package cn.frank.jesquedemo.service;

import lombok.extern.slf4j.Slf4j;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.client.ClientPoolImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DemoService {

  @Autowired
  private ClientPoolImpl submitJobClient;

  @Value("${jesque.queue}")
  private String queueName;


  public void submit(List<String> sentences) {
    Job job = new Job("DemoJob", Arrays.asList(sentences));
    submitJobClient.enqueue(queueName, job);
  }

  public void submitUpgrade(List<String> sentences) {
    Job job = new Job("DemoJobUpgrade", Arrays.asList(sentences));
    submitJobClient.enqueue(queueName, job);
  }

  public void submitAll(List<String> sentences) {

    List<Job> jobs = sentences.stream().map(i -> new Job("SendDataJob", Arrays.asList(i))).collect(
        Collectors.toList());
    submitJobClient.batchEnqueue(queueName, jobs);
  }


}
