package cn.frank.jesquedemo.controller;

import cn.frank.jesquedemo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class DemoController {

  @Autowired
  private DemoService demoService;

  @PostMapping("/submitDemoJobUpgrade")
  public Object submitDemoJobUpgrade(@RequestBody List<String> sentences) {
    demoService.submitDemoJobUpgrade(sentences);
    return "submitDemoJobUpgrade OK \n";
  }

  @PostMapping("/submitBatchSendDataJob")
  public Object getDemo2(@RequestBody List<String> sentences) {
    demoService.submitBatchSendDataJob(sentences);
    return "submitBatchSendDataJob OK \n";
  }

}
