package com.example.final_project.util;


import com.example.final_project.model.dto.request.material.MultipleDelete;
import java.util.List;

public class MaterialSqlScript {
    public static String deleteMaterials(MultipleDelete materials){
        List<Integer> materialList = materials.getMaterialIds();
        StringBuilder sql = new StringBuilder("DELETE FROM material WHERE material_id IN (");
        for(int i = 0; i < materialList.size(); i++){
            sql.append(materialList.get(i));
            if(i < materialList.size() - 1)
                sql.append(",");
        }
        sql.append(");");
        return sql.toString();
    }

    public static String getMaterials(MultipleDelete materials){
        List<Integer> materialList = materials.getMaterialIds();
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM material WHERE material_id IN (");
        for(int i = 0; i < materialList.size(); i++){
            sql.append(materialList.get(i));
            if(i < materialList.size() - 1)
                sql.append(", ");
        }
        sql.append(");");
        return sql.toString();
    }
}
