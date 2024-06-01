package com.example.final_project.util;

public class RegistrationFormString {
    public static String getConferenceString(){
        return """
                { "form" :
                    [
                         {
                             "id": "1",
                             "type": "text",
                             "label": "Full Name",
                             "header": "Contact Information"
                         },
                         {
                             "id": "2",
                             "type": "drop_down",
                             "label": "Gender",
                             "items": ["Male", "Female"],
                             "header": "Contact Information"
                         },
                         {
                             "id": "3",
                             "type": "text",
                             "label": "Phone Number",
                             "header": "Contact Information"
                         },
                         {
                             "id": "4",
                             "type": "text",
                             "label": "Email",
                             "header": "Contact Information"
                         },
                         {
                             "id": "5",
                             "type": "text",
                             "label": "Company Name",
                             "header": "Affiliation"
                         },
                         {
                             "id": "6",
                             "type": "text",
                             "label": "Title/Position (optional)",
                              "header": "Affiliation"
                         }
                     ]
                     }
                """;
        }
        public static String getMarathonString(){
            return """
                    { "form" : [
                        {
                            "id": "1",
                            "type": "text",
                            "label": "Full Name",
                            "header": "Contact Information"
                        },
                        {
                            "id": "2",
                            "type": "drop_down",
                            "label": "Gender",
                            "items": ["Male", "Female"],
                            "header": "Contact Information"
                        },
                        {
                            "id": "3",
                            "type": "text",
                            "label": "Phone Number",
                            "header": "Contact Information"
                        },
                        {
                            "id": "4",
                            "type": "text",
                            "label": "Email",
                            "header": "Contact Information"
                        },
                        {
                            "id": "5",
                            "type": "drop_down",
                            "label": "Race Distance",
                            "items": ["Marathon", "Haft Marathon", "5K"],
                            "header": "Event Participation"
                        },
                        {
                            "id": "6",
                            "type": "date",
                            "label": "Date of Birth",
                            "header": "Event Participation"
                    
                        },
                        {
                            "id": "7",
                            "type": "text",
                            "label": "Emergency Contact Name",
                            "header": "Event Participation"
                        },
                        {
                            "id": "8",
                            "type": "text",
                            "label": "Emergency Contact Phone",
                            "header": "Event Participation"
                        }
                    ]
                   }
            """;
    }
    public static String getUnknownCategoryString(){
        return """
                    { "form " : [
                        {
                            "id": "1",
                            "type": "text",
                            "label": "Full Name",
                            "header": "Contact Information"
                        },
                        {
                            "id": "2",
                            "type": "text",
                            "label": "Phone Number",
                            "header": "Contact Information"
                        }
                    ]
                """;
    }
}
