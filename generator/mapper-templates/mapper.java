package ${package};

import ${project.attrs.basePackage}.model.${it.name.className};
import org.apache.ibatis.annotations.Mapper;

/**
 * ${it.name} - ${it.comment}
 *
 * @author ${SYS['user.name']}
 */
@Mapper
public interface ${it.name.className}Mapper extends BaseMapper<${it.name.className}> {

}