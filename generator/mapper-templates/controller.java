package ${package};

import io.mybatis.common.core.DataResponse;
import io.mybatis.common.core.RowsResponse;

import ${project.attrs.basePackage}.model.${it.name.className};
import ${project.attrs.basePackage}.service.${it.name.className}Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${it.name} - ${it.comment}
 *
 * @author ${SYS['user.name']}
 */
@RestController
@RequestMapping("${it.name.fieldName.s}")
public class ${it.name.className}Controller {

  @Autowired
  private ${it.name.className}Service ${it.name.fieldName}Service;

  @PostMapping
  public DataResponse<${it.name.className}> save(${it.name.className}  ${it.name.fieldName}) {
    return DataResponse.ok(${it.name.fieldName}Service.save( ${it.name.fieldName}));
  }

  @GetMapping
  public RowsResponse<${it.name.className}> findList(${it.name.className}  ${it.name.fieldName}) {
    return RowsResponse.ok(${it.name.fieldName}Service.findList( ${it.name.fieldName}));
  }

  @GetMapping(value = "/{id}")
  public DataResponse<${it.name.className}> findById(@PathVariable("id") Long id) {
    return DataResponse.ok(${it.name.fieldName}Service.findById(id));
  }

  @PutMapping(value = "/{id}")
  public DataResponse<${it.name.className}> update(@PathVariable("id") Long id, ${it.name.className}  ${it.name.fieldName}) {
     ${it.name.fieldName}.setId(id);
    return DataResponse.ok(${it.name.fieldName}Service.update( ${it.name.fieldName}));
  }

  @DeleteMapping(value = "/{id}")
  public DataResponse<Boolean> deleteById(@PathVariable("id") Long id) {
    return DataResponse.ok(${it.name.fieldName}Service.deleteById(id) == 1);
  }

}
