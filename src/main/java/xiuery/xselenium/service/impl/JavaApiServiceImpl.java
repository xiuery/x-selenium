package xiuery.xselenium.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xiuery.xselenium.entity.JavaApi;
import xiuery.xselenium.mapper.JavaApiMapper;
import xiuery.xselenium.service.JavaApiService;

@Service
public class JavaApiServiceImpl extends ServiceImpl<JavaApiMapper, JavaApi> implements JavaApiService {
}
