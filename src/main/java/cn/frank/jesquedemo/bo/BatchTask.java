package cn.frank.jesquedemo.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchTask {

  @JsonIgnore
  @JSONField(serialize = false)
  private static Logger log = LoggerFactory.getLogger(BatchTask.class);

  private String taskId;
  private String type;
  private String vargs;
  private Integer status;
  private Integer subTasks;
  private Integer success;
  private Integer fails;
  private Long createTime;
  private Long finishedTime;
  private String errors;

  public static String[] getDeclaredFields() {
    Field[] fields = BatchTask.class.getDeclaredFields();
    List<String> res = Arrays.asList(fields)
        .stream()
        .filter(fi -> !Modifier.isStatic(fi.getModifiers()))
        .map(Field::getName)
        .collect(Collectors.toList());
    return res.toArray(new String[0]);
  }

  public static BatchTask buildFromList(List<String> vals) {
    Preconditions.checkArgument(vals != null && vals.size() == 10, "not enough vals");
    return new BatchTask(
        vals.get(0),
        vals.get(1),
        vals.get(2),
        safeFromString(vals.get(3)),
        safeFromString(vals.get(4)),
        safeFromString(vals.get(5)),
        safeFromString(vals.get(6)),
        safeLongFromString(vals.get(7)),
        safeLongFromString(vals.get(8)),
        vals.get(9)
    );
  }

  static Integer  safeFromString(String in) {
    try {
      return Integer.valueOf(in);
    } catch (Throwable e) {
      return null;
    }
  }
  static Long safeLongFromString(String in) {
    try {
      return Long.valueOf(in);
    } catch (Throwable e) {
      return null;
    }
  }

  public static Map<String, String> toMap(BatchTask batchTask) {
    Field[] fields = BatchTask.class.getDeclaredFields();
    Map<String, String> res = Maps.newHashMap();

    for (Field field : fields) {
      if (!Modifier.isStatic(field.getModifiers())) {
        field.setAccessible(true);
        try {
          res.put(field.getName(),
              Optional.ofNullable(field.get(batchTask)).map(String::valueOf).orElse(""));
        } catch (Throwable e) {
          log.warn("{}", ExceptionUtils.getStackTrace(e));
        }
      }
    }
    return res;
  }


}
