
# EventMaster

> [!NOTE]
>  1. JDK 21 required for all installation methods
>  2. Install Postgres : <br> host = 110.74.194.124 <br> port = 5440 <br> username = gatherly <br> password = gatherly
      
## Getting started
## API endpoint
### 1. Landing-page-controller
####  a. Search and filter:
> [/api/landing-page/search](http://34.124.203.109/swagger-ui/index.html#/landing-page-controller/searchEvent)
#### b. Get all event by categories:
> [/api/landing-page](http://34.124.203.109/swagger-ui/index.html#/landing-page-controller/getAllEventsByCategory)
#### c. Get detail event by event id:
> [/api/landing-page/{eventId}](http://34.124.203.109/swagger-ui/index.html#/landing-page-controller/getDetailEventByEventId)
#### d. Get form by event id:
> [/api/landing-page/form/{eventId}](http://34.124.203.109/swagger-ui/index.html#/landing-page-controller/getFormByEventId)

### 2. Auth-controller 
#### a. Register for admin:
> [/api/auth/admin-register](http://34.124.203.109/swagger-ui/index.html#/auth-controller/adminRegister)
-- after registrater as an admin, an organization will be created
#### b. Register for user:
> [/api/auth/user-register](http://34.124.203.109/swagger-ui/index.html#/auth-controller/userRegister)
#### c. Verify email by OTP code:
> [/api/auth/verify](http://34.124.203.109/swagger-ui/index.html#/auth-controller/verifyOTP)
#### d. Log in to dashboard:
> [/api/auth/login](http://34.124.203.109/swagger-ui/index.html#/auth-controller/authenticate)
#### e. Get organization by organization code:
> [/api/auth/org/{code}](http://34.124.203.109/swagger-ui/index.html#/auth-controller/getOrganizationByCode)
#### f. Resend OTP code to email:
> [/api/auth/resend](http://34.124.203.109/swagger-ui/index.html#/auth-controller/resendOTP)
#### g. Set new password:
> [/api/auth/set-new-password](http://34.124.203.109/swagger-ui/index.html#/auth-controller/forgetPassword)

### 3. Dashboard-controller
####  a. Get dashboard data:
> [/api/dashboards](http://34.124.203.109/swagger-ui/index.html#/dashboard-controller/getDashboardData)

### 4. Member-controller
####  a. Get all members:
> [/api/members](http://34.124.203.109/swagger-ui/index.html#/member-controller/getAllMembers)
#### b. Search by name:
> [/api/members/search](http://34.124.203.109/swagger-ui/index.html#/member-controller/searchMemberByName)
#### c. Change role:
> [/api/members/{memberId}](http://34.124.203.109/swagger-ui/index.html#/member-controller/updateMemberRole)
#### d. Delete member by id:
> [/api/members/{memberId}](http://34.124.203.109/swagger-ui/index.html#/member-controller/deleteMemberById)

### 5. Category-controller
####  a. Get all categories:
> [/api/categories](http://34.124.203.109/swagger-ui/index.html#/category-controller/getAllCategories)
#### b. Create new category:
> [/api/categories](http://34.124.203.109/swagger-ui/index.html#/category-controller/createCategory)
#### c. Update category by id:
> [/api/categories/{categoryId}](http://34.124.203.109/swagger-ui/index.html#/category-controller/updateCategory)
#### d. Delete category by id:
> [/api/categories/{categoryId}](http://34.124.203.109/swagger-ui/index.html#/category-controller/deleteCategory)

### 6. Event-controller
####  a. Get all events:
> [/api/events](http://34.124.203.109/swagger-ui/index.html#/event-controller/getAllEvents)
#### b. Get event by id:
> [/api/events/{eventId}](http://34.124.203.109/swagger-ui/index.html#/event-controller/getEventById)
#### c. Create new event:
> [/api/events](http://34.124.203.109/swagger-ui/index.html#/event-controller/createEvent)
#### d. Search and filter:
> [/api/events/search](http://34.124.203.109/swagger-ui/index.html#/event-controller/searchEvent_1)
#### e. Update status event by id:
> [/api/events/active/{eventId}](http://34.124.203.109/swagger-ui/index.html#/event-controller/updateActiveById)
#### f. Update registration form by event id:
> [/api/events/registration-form/{eventId}](http://34.124.203.109/swagger-ui/index.html#/event-controller/modifyRegistrationForm)
#### g. Update event by id:
> [/api/events/{eventId}](http://34.124.203.109/swagger-ui/index.html#/event-controller/updateEventById)
#### h. Delete event by id:
> [/api/events/{eventId}](http://34.124.203.109/swagger-ui/index.html#/event-controller/deleteEventById)
#### Example:
-- category name : Conferences
```
{
    "form" :[
              { "fieldType": "text,Name" },
              { "fieldType": "gender,Gender" },
              { "fieldType": "tel,Phone" },
              { "fieldType": "email,Email" },
              { "fieldType": "text,Company Name" },
              { "fieldType": "text,Title/Position" }
    ]
}

```
-- category name Marathons And Races

```
{ 
       "form" :[
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

```
-- other categories
```
{  
    "form" :[
              { "fieldType": "text,Name" },
              { "fieldType": "tel,Phone"}
    ]
}

```

### 7. User-request-controller
####  a. Get all user requests:
> [/api/user-requests](http://34.124.203.109/swagger-ui/index.html#/user-request-controller/getAllUserRequest)
#### b. Update user request by id:
> [/api/user-requests/approve/{memberId}](http://34.124.203.109/swagger-ui/index.html#/user-request-controller/approveMember)
#### c. Delete user request by id:
> [/api/user-requests/reject/{memberId}](http://34.124.203.109/swagger-ui/index.html#/user-request-controller/rejectMember)

### 8. Asset-controller
####  a. Get all assets:
> [/api/assets](http://34.124.203.109/swagger-ui/index.html#/asset-controller/getAllAsset)
#### b. Get asset by id:
> [/api/assets/{assetId}](http://34.124.203.109/swagger-ui/index.html#/asset-controller/getAssetById)
#### c. Create new asset:
> [/api/assets/create](http://34.124.203.109/swagger-ui/index.html#/asset-controller/createAsset)
#### d. Search asset by name:
> [/api/assets/search](http://34.124.203.109/swagger-ui/index.html#/asset-controller/getAssetByName)
#### e. Update asset by id:
> [/api/assets/update/{assetId}](http://34.124.203.109/swagger-ui/index.html#/asset-controller/UpdateAsset)
#### f. Delete asset by id:
> [/api/assets/delete/{assetId}](http://34.124.203.109/swagger-ui/index.html#/asset-controller/deleteMemberById_1)

### 9. Agenda-controller
####  a. Get agenda by event id:
> [/api/agendas/{eventId}](http://34.124.203.109/swagger-ui/index.html#/agenda-controller/getAgendaByEventId)
#### b. Create agenda by event id:
> [/api/agendas/{eventId}](http://34.124.203.109/swagger-ui/index.html#/agenda-controller/createAgenda)
#### c. Update agenda by event id:
> [/api/agendas/{eventId}](http://34.124.203.109/swagger-ui/index.html#/agenda-controller/updateAgendaById)
#### d. Delete agenda by event id:
> [/api/agendas/{eventId}](http://34.124.203.109/swagger-ui/index.html#/agenda-controller/deleteAgendaByEventId)
##### Example:
```
{
    "data": {
        "agenda" : [
            {
                "startTime" : "3:00PM",
                "endTime" : "4:00PM",
                "topic": "Wake Up",
                "description": "Start your day with energy and enthusiasm."
            },
            {
                "startTime" : "4:00PM",
                "endTime" : "5:00PM",
                "topic": "Game&Activity",
                "description": "Fun game and activity booths all around food and drink on offer"
            },
            {
                "startTime" : "5:00PM",
                "endTime" : "7:00PM",
                "topic": "Presentation"
            }
        ]
    }
}
```

### 10. Attendee-controller
####  a. Get attendee by event id:
> [/api/attendees/{eventId}](http://34.124.203.109/swagger-ui/index.html#/attendee-controller/getAttendeesByEventId)
#### b. Create attendee:
> [/api/attendees/create](http://34.124.203.109/swagger-ui/index.html#/attendee-controller/createAttendee)
#### c. Search attendee by name:
> [/api/attendees/search](http://34.124.203.109/swagger-ui/index.html#/attendee-controller/searchAttendeeByNameOrPhone)
#### d. Delete attendee by id:
> [/api/attendees/search](http://34.124.203.109/swagger-ui/index.html#/attendee-controller/deleteAttendeeById)

### 11. Profile-controller
####  a. Get profile member:
> [/api/profiles](http://34.124.203.109/swagger-ui/index.html#/profile-controller/getProfile)
#### b. Get profile organization:
> [/api/profiles/organization](http://34.124.203.109/swagger-ui/index.html#/profile-controller/getOrganization)
#### c. Update profile by id:
> [/api/profiles/update-member/{memberId}](http://34.124.203.109/swagger-ui/index.html#/profile-controller/updateProfile)
#### d. Update profile organization by id:
> [/api/profiles/update-organization/{orgId}](http://34.124.203.109/swagger-ui/index.html#/profile-controller/updateOrganization)
#### e. Change password:
> [/api/profiles/change-password](http://34.124.203.109/swagger-ui/index.html#/profile-controller/changePassword)

### 12. Material-controller
####  a. Get all materials by event id:
> [/api/materials/getAll/{eventId}](http://34.124.203.109/swagger-ui/index.html#/material-controller/getAllMaterials)
#### b. Get material by id:
> [/api/materials/{materialId}](http://34.124.203.109/swagger-ui/index.html#/material-controller/getMaterialById)
#### c. Get count status:
> [/api/materials/count-status/{eventId}](http://34.124.203.109/swagger-ui/index.html#/material-controller/getAllMaterialsCount)
#### d. Create material:
> [/api/materials/create](http://34.124.203.109/swagger-ui/index.html#/material-controller/createMaterial)
#### e. Search material by name:
> [/api/materials/search](http://34.124.203.109/swagger-ui/index.html#/material-controller/SearchMaterialByName)
####  a. Update handler by material id:
> [/api/materials/handler/{materialId}](http://34.124.203.109/swagger-ui/index.html#/material-controller/updateHandlerByMaterialId)
#### b. Update material status by id:
> [/api/materials/status/{materialId}](http://34.124.203.109/swagger-ui/index.html#/material-controller/updateMaterialStatus)
#### c. Update supporters by material id:
> [/api/materials/supporters/{materialId}](http://34.124.203.109/swagger-ui/index.html#/material-controller/updateSupportersByMaterialId)
#### d. Delete material by id:
> [/api/materials/delete/{materialId}](http://34.124.203.109/swagger-ui/index.html#/material-controller/deleteMaterialById)
#### e. Deletes material by id:
> [/api/materials/deletes](http://34.124.203.109/swagger-ui/index.html#/material-controller/deletesMaterialByIds)
#### Example:
```
{
  "supporters": {
    "data": [
        {
            "name" : "Menglim",
            "profile" : "123.png"
        },
        {
            "name" : "Dara",
            "profile" : "123.png"
        }
    ]
  }
}

```
