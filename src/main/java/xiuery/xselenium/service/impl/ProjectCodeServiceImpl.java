package xiuery.xselenium.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xiuery.xselenium.entity.ProjectCode;
import xiuery.xselenium.mapper.ProjectCodeMapper;
import xiuery.xselenium.service.ProjectCodeService;

@Service
public class ProjectCodeServiceImpl extends ServiceImpl<ProjectCodeMapper, ProjectCode> implements ProjectCodeService {
}
