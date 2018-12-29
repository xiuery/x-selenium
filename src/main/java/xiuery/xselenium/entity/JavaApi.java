package xiuery.xselenium.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "x_java_api")
public class JavaApi extends BaseEntity {

    private String name;
    private int method;
    private String description;
    private String exampleTitle;
    private String project;
    private String file;
    private String code;
    private String path;
}
