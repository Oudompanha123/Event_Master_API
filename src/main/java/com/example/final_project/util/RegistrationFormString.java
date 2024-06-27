package com.example.final_project.util;

public class RegistrationFormString {
    public static String getConferenceString(){
        return """
                { "form" :
                    [
                        { "fieldType": "text,Name" },
                        { "fieldType": "gender,Gender" },
                        { "fieldType": "tel,Phone" },
                        { "fieldType": "email,Email" },
                        { "fieldType": "text,Company Name" },
                        { "fieldType": "text,Title/Position" }
                     ]
                     }
                """;
        }
        public static String getMarathonString(){
            return """
                    { "form" : [
                        { "fieldType": "text,Name" },
                        { "fieldType": "gender,Gender" },
                        { "fieldType": "tel,Phone" },
                        { "fieldType": "email,Email" },
                        { "fieldType": "text,Race Distance" },
                        { "fieldType": "date,Date of Birth" },
                        { "fieldType": "text,Emergency Contact Name" },
                        { "fieldType": "tel,Emergency Contact Phone" }
                    ]
                   }
            """;
    }
    public static String getUnknownCategoryString(){
        return """
                    {
                        "form": [
                            { "fieldType": "text,Name" },
                            { "fieldType": "tel,Phone"}
                        ]
                    }
                """;
    }
}
