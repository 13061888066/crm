package com.mage.crm.dao;

import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleDao {
    List<ModuleDto> queryAllsModuleDtos();

    Module queryModuleById(Integer moduleId);

    List<Module> queryModulesByParams(ModuleQuery moduleQuery);

    List<Module> queryModulesByGrade(Integer grade);

    Module queryModuleByOptValue(String optValue);

    Module queryModuleByGradeAndModuleName(@Param("grade")Integer grade,@Param("moduleName") String moduleName);

    Module queryModuleByParentId(Integer parentId);

    int insert(Module module);

    int update(Module module);

    List<Module> querySubModulesByParentId(Integer parentId);

    int delete(List<Integer> ids);
}
