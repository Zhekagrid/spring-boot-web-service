package com.example.webservice.utill.mapper;

import com.example.webservice.model.dto.EmployeeFormDto;
import com.example.webservice.model.entity.Employee;
import org.mapstruct.Mapper;

/**
 * {@link Employee} mapper
 */
@Mapper
public interface EmployeeMapper {
    /**
     * Maps {@link EmployeeFormDto} to {@link Employee}
     *
     * @param employeeFormDto the {@link EmployeeFormDto} object for mapping
     * @return {@link Employee} mapped from employeeFormDto
     */
    Employee employeeDtoToEmployee(EmployeeFormDto employeeFormDto);

    /**
     * Maps {@link Employee}  to {@link EmployeeFormDto}
     *
     * @param employee the {@link Employee} object for mapping
     * @return {@link EmployeeFormDto} mapped from employeeo
     */
    EmployeeFormDto employeeToEmployeeDto(Employee employee);
}
