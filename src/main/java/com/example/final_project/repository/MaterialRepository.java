package com.example.final_project.repository;

import com.example.final_project.model.MaterialStatusCount;
import com.example.final_project.model.constant.Status;
import com.example.final_project.model.dto.request.material.MultipleDelete;
import com.example.final_project.model.dto.response.material.MaterialResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MaterialRepository {

    @Select("""
        SELECT * FROM material
    """)
    @Results(id = "materialMapper", value = {
            @Result(property = "materialId", column = "material_id"),
            @Result(property = "materialName", column = "material_name"),
            @Result(property = "assignDate", column = "assign_date"),
            @Result(property = "dueDate", column = "due_date"),
            @Result(property = "handlerId", column = "handler_id"),
            @Result(property = "supporters", column = "supporters")
    })
    List<MaterialResponse> getAllMaterial();

    @Select("""
        select
            Count(*) as total,
            Count(case when status='Pending' then 1  end) as pending,
            Count(case when status='OnGoing' then 1  end) as onGoing,
            Count(case when status='Done' then 1 end) as done,
            Count(case when status='Issue' then 1 end) as issue
        from material WHERE event_id = #{eventId};
    """)
    List<MaterialStatusCount> getMaterialStatusCount(Integer eventId);

    @Select("""
        SELECT COUNT(*) FROM material WHERE event_id = #{eventId};
    """)
    Integer totalMaterial(Integer eventId);

    @Select("""
        UPDATE material SET status = #{status} WHERE material_id = #{materialId} RETURNING *;
    """)
    @ResultMap("materialMapper")
    MaterialResponse updateMaterialStatus(Integer materialId, Status status);

    @Delete("""
        DELETE FROM material WHERE material_id = #{materialId};
    """)
    void deleteMaterialById(Integer materialId);

    @Delete("""
        DELETE FROM material WHERE material_id IN #{ids.materialIds.materialIds};
    """)
    void deleteMaterialByIds(@Param("ids") MultipleDelete materialIds);


}