
## EventMaster

> [!NOTE]
>  1. JDK 21 required for all installation methods
>  2. Install Postgres
      
### Getting started
### Usage
#### AuthController
http://localhost:8080/swagger-ui/index.html#/auth-controller
> step 1: You need to register admin in organization
> <br>
> http://localhost:8080/swagger-ui/index.html#/auth-controller/adminRegister
> <br><br>
> step 2: You need to register user with organization code
> <br>
> http://localhost:8080/swagger-ui/index.html#/auth-controller/userRegister
> <br><br>
> step 3: You need to verify email by code that within gmail account
> <br>
> http://localhost:8080/swagger-ui/index.html#/auth-controller/verifyOTP
> <br><br>
> step 4: You logged in to your account. If you are the user, please wait for admin to approve
> <br>
> http://localhost:8080/swagger-ui/index.html#/auth-controller/authenticate
> <br><br>
> step 5: You can forget password their email and then code will send to their email. Do not need to log in account
> <br>
> http://localhost:8080/swagger-ui/index.html#/auth-controller/forgetPassword
> <br><br>
> step 6: You need to register user with organization code
> <br>
> http://localhost:8080/swagger-ui/index.html#/auth-controller/userRegister
> <br><br>
> step 7: Admin and user can re-request OTP code.
> <br>
> http://localhost:8080/swagger-ui/index.html#/auth-controller/verifyOTP
> <br><br>
> step 8: You logged in to your account. If you are the user, please wait for admin to approve
> <br>
> http://localhost:8080/swagger-ui/index.html#/auth-controller/authenticate<br>