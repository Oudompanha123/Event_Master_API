package com.example.final_project.repository;

import com.example.final_project.model.MaterialStatusCount;
import com.example.final_project.model.constant.Status;
import com.example.final_project.model.dto.request.material.MaterialRequestForCreating;
import com.example.final_project.model.dto.request.material.MaterialRequestForUpdating;
import com.example.final_project.model.dto.request.material.MultipleDelete;
import com.example.final_project.model.Material;
import com.example.final_project.model.dto.response.material.MaterialResponse;
import com.example.final_project.util.MaterialSqlScript;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MaterialRepository {

    @Select("""
        SELECT material_id, material_name, qty, unit, assign_date, due_date,
               coalesce(member.member_name, member_history.member_name) AS handlerName,
               coalesce(member.member_id, member_history.member_id) AS handlerId,
               coalesce(member.picture, member_history.picture) AS picture,
               supporters, status, remark
        FROM (material LEFT JOIN member ON material.handler_id = member.member_id)
            LEFT JOIN member_history ON material.handler_id = member_history.member_id
        WHERE event_id = #{eventId} ORDER BY due_date ;
    """)
    @Results(id = "materialMapper", value = {
            @Result(property = "materialId", column = "material_id"),
            @Result(property = "materialName", column = "material_name"),
            @Result(property = "assignDate", column = "assign_date"),
            @Result(property = "dueDate", column = "due_date")
    })
    List<Material> getAllMaterial(Integer eventId);

    @Select("""
        select
            Count(*) as total,
            Count(case when status='Pending' then 1  end) as pending,
            Count(case when status='OnGoing' then 1  end) as onGoing,
            Count(case when status='Done' then 1 end) as done,
            Count(case when status='Issue' then 1 end) as issue
        from material WHERE event_id = #{eventId};
    """)
    MaterialStatusCount getMaterialStatusCount(Integer eventId);

    @Delete("""
        DELETE FROM material WHERE material_id = #{materialId};
    """)
    void deleteMaterialById(Integer materialId);

    @SelectProvider(type = MaterialSqlScript.class, method = "deleteMaterials")
    void deleteMaterialByIds(MultipleDelete materialIds);

    @SelectProvider(type = MaterialSqlScript.class, method = "getMaterials")
    Integer getMaterialByIds(MultipleDelete materialIds);

    @Select("""
        SELECT material_id, material_name, qty, unit, assign_date, due_date,
               coalesce(member.member_name, member_history.member_name) AS handlerName,
               coalesce(member.member_id, member_history.member_id) AS handlerId,
               coalesce(member.picture, member_history.picture) AS picture,
               supporters, status, remark
        FROM (material LEFT JOIN member ON material.handler_id = member.member_id)
            LEFT JOIN member_history ON material.handler_id = member_history.member_id
        WHERE material_id = #{materialId};
    """)
    @ResultMap("materialMapper")
    Material getMaterialById(Integer materialId);

    @Select("""
        SELECT material_id FROM material WHERE material_id = #{materialId} AND event_id = #{eventId};
    """)
    Integer getMaterialIdByMaterialId(Integer materialId, Integer eventId);

    @Select("""
        SELECT event_id FROM material;
    """)
    List<Integer> getAllEventIdInMaterialTable();

    @Select("""
        SELECT material_id, material_name, qty, unit, assign_date, due_date,
               coalesce(member.member_name, member_history.member_name) AS handlerName,
               coalesce(member.member_id, member_history.member_id) AS handlerId,
               coalesce(member.picture, member_history.picture) AS picture,
               supporters, status, remark
        FROM (material LEFT JOIN member ON material.handler_id = member.member_id)
            LEFT JOIN member_history ON material.handler_id = member_history.member_id
        WHERE event_id = #{eventId} AND material_name ILIKE CONCAT('%', #{materialName}, '%') ;
    """)
    @ResultMap("materialMapper")
    List<Material> searchMaterialByName(String materialName, Integer eventId);

    @Select("""
        INSERT INTO material
        VALUES (default, #{material.materialName}, #{material.qty}, #{material.unit}, 'No Remark',
         #{material.status}, default, #{material.dueDate}, #{material.handlerId},
        #{material.supporters, typeHandler = com.example.final_project.config.JsonbTypeHandler} :: JSONB, #{material.eventId})
        RETURNING *
    """)
    @ResultMap("materialMapper")
    @Result(property = "handlerId", column = "handler_id")
    @Result(property = "eventId", column = "event_id")
    Material createMaterial(@Param("material") MaterialRequestForCreating materialRequest);

    @Select("""
        UPDATE material
        SET material_name = #{material.materialName},
            qty = #{material.qty},
            unit = #{material.unit},
            status = #{material.status},
            due_date = #{material.dueDate},
            handler_id = #{material.handlerId},
            supporters = #{material.supporters, typeHandler = com.example.final_project.config.JsonbTypeHandler} :: JSONB
        WHERE material_id = #{materialId}
    """)
    MaterialResponse updateMaterialDataByMaterialId(Integer materialId,@Param("material") MaterialRequestForUpdating materialRequestForUpdating);

    @Select("""
        UPDATE material SET status = #{status} WHERE material_id = #{materialId};
    """)
    void updateMaterialStatus(Integer materialId, Status status);
}