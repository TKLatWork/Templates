package my.template.model.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(
        description = "通用的返回格式"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult<T> {
    public static final Integer STATUS_OK = 0;
    public static final Integer STATUS_API_ERROR = -1;
    public static final Integer STATUS_SYS_ERROR = -2;
    @ApiModelProperty(
            notes = "状态代码，成功:0, 业务失败:-1，系统错误:-2。其他自定。"
    )
    private Integer status;
    private String message;
    private T result;
}
