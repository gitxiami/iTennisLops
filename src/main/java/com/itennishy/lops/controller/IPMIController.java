package com.itennishy.lops.controller;

import com.itennishy.lops.executor.JSchExecutor;
import com.itennishy.lops.utils.DeviceDiscoveryUtils;
import com.itennishy.lops.utils.FileUtils;
import com.itennishy.lops.utils.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/ipmi")
public class IPMIController {

    /**
     * 批量做Raid
     *
     * @param conf
     * @return
     */
    @RequestMapping("/config")
    public JsonData setRaid(String conf) {
        Vector<Map<String, String>> vector = new Vector<>();
        try {
            List<String[]> contents = new FileUtils().getConfigContent(conf);
            List<FutureTask<Map<String, String>>> futureTasks = new ArrayList<>();
            for (String[] content : contents) {
                futureTasks.add(new FutureTask<>(new Callable<Map<String, String>>() {
                    @Override
                    public Map<String, String> call() throws Exception {
                        Map<String, String> result = new HashMap<>();
                        JSchExecutor jSchUtil = new JSchExecutor();
                        try {
                            if (new DeviceDiscoveryUtils().getOnlineDevice(content[0])) {
                                jSchUtil = new JSchExecutor(content[1], content[2], content[0]);
                                jSchUtil.connect();
                                int status = jSchUtil.execCmd("yum list installed | grep ipmitool");
                                if (status != 0) {
                                    try {
                                        jSchUtil.execCmd("yum install -y ipmitool");
                                    } catch (Exception e) {
                                        log.error("----安装缺失软件:ipmitool", e.getMessage());
                                        result.put(content[0], null);
                                        return result;
                                    }
                                }
                                LinkedList<String> cmds = new LinkedList<>();
                                cmds.add("modprobe ipmi_watchdog");
                                cmds.add("modprobe ipmi_poweroff");
                                cmds.add("modprobe ipmi_devintf");
                                cmds.add("modprobe ipmi_si");
                                cmds.add("ipmitool chassis power status");
                                cmds.add("ipmitool lan set 1 ipsrc static");
                                cmds.add("ipmitool lan set 1 ipaddr " + content[3]);
                                cmds.add("ipmitool lan set 1 netmask " + content[4]);
                                cmds.add("ipmitool lan set 1 defgw ipaddr " + content[5]);

                                for (String cmd : cmds) {
                                    status = jSchUtil.execCmd(cmd);
                                    if (status != 0){
                                        log.error("执行命令出现错误");
                                    }
                                }

                                result.put(content[0], jSchUtil.getStandardOutput().toString());
                            } else {
                                result.put(content[0], null);
                            }
                        } catch (Exception e) {
                            log.error("执行命令出现异常:", e);
                            result.put(content[0], null);
                        } finally {
                            jSchUtil.disconnect();
                        }
                        return result;
                    }
                }));
            }

            if (contents.size() == 0) {
                return JsonData.BuildSuccess("配置文件没有内容，请核实查看");
            }
            ExecutorService executorService = Executors.newFixedThreadPool(contents.size());
            for (FutureTask<Map<String, String>> futureTask : futureTasks) {
                executorService.submit(futureTask);
            }
            executorService.shutdown();
            for (int i = 0; i < contents.size(); i++) {
                try {
                    Map<String, String> flag = futureTasks.get(i).get();
                    vector.add(flag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            return JsonData.BuildError(5001, e.getMessage());
        }
        return JsonData.BuildSuccess(vector);
    }

}
