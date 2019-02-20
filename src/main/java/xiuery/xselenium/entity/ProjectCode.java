package xiuery.xselenium.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "x_project_code")
public class ProjectCode extends BaseEntity {


    private String name;
    private String lang;
    private String description;
    private String path;

}
