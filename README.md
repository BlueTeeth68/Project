[# Read manga online website

# Function list:

- Register by Email, facebook or username and password
- Reset password
- Update profile (display name, avatar)
- Manage user (change role, activate, deactivate)
- Notification
- Search mangas by name, genre, author, translator,...
- Sort mangas by views (day, week, month,...)
- Suggest manga
- Register for translator account
- Rate manga
- Report manga, user, translator
- Add manga to favorite (bookmark)
- View reading history, comment history
- Comment, reply comment

# Framework

- Spring boot, Spring security, Spring dataJPA
- Hibernate
- JUnit 5

# Environment

- IDE: Intellij
- Maven
- jdk 17
- Database: MySQL

# API:

**0. Authenticate and Authorize:**

- Default admin account:

+ admin: username: SystemAdmin, password: 0000
+ user: username: user, password: 0000

**0.1 Register new account:**

- Require: none
- URL: /api/auth/register
- Method: POST
- Request param: none
- Request body: UsernamePasswordVM (String username, String password)
- Response body: Token(String token)
- Description: User can register new account by providing username and password

**0.2 Authorize account:**

- Require: none
- URL: /api/auth/authenticate
- Method: POST
- Request param: none
- Request body: UsernamePasswordVM (String username, String password)
- Response body: Token(String token)
- Description: User can log in to the system by using username and password

**1. User:**

**1.1 Get user list:**

- Require: ADMIN
- URL: /admin/users
- Method: GET
- Request param: none
- Request body: none
- Response body: List<<User>> or not found exception
- Description: Admin can get all user in the database

**1.2 Get user by id/username:**

- Require: ADMIN
- URL: /admin/user
- Method: GET
- Request param: id/username
- Request body: none
- Response body: User or not found
- Description: Admin can get user by id or username. The response is User with almost properties.

**Change user role**

- Require: ADMIN
- URL: /admin/user/role
- Method: PATCH
- Request param: id, role (USER, ADMIN, TRANSLATOR)
- Request body: none
- Response body: User or BadRequestException
- Description: Admin can change role of a user

**Activate/Deactivate user**

- Require: ADMIN
- URL: /admin/user/activate-status
- Method: PATCH
- Request param: id, status (true/false)
- Request body: none
- Response body: User or BadRequestException
- Description: Admin can activate or deactivate a user. User is deactivated can not log in to website

**Get current user**

- Require: authenticated account
- URL: /account
- Method: GET
- Request param: None
- Request body: none
- Response body: User
- Description: Return the user is logging into the website

**Update displayName**

- Require: authenticated account
- URL: /account/display-name
- Method: PATCH
- Request param: id, displayName
- Request body: none
- Response body: User or DataAlreadyExistsException
- Description: user can change display name that has not been existed in the system

**Update avatar:**

- Require: authenticated user
- URL: /account/avatar
- Method: PATCH (Thường thì phải dùng post, patch thì chưa test nên không biết được không)
- Request param: none
- Request body: id, file image
- Response body: User
- Description: user can update avatar by providing an image

**Get user by id/username:**

- Require: authenticated user
- URL: /account/user
- Method: GET
- Request param: id/username
- Request body: none
- Response body: CommonUserDTO or ResourceNotFoundException
- Description: user can view others profile with public information

**Change password**

**Reset password**

**Link with Google account**

**Link with Facebook account**

**Remove Google account**

**Remove Facebook account**

**Change Google account**

**Change Facebook account**

**Delete account**

**2. Manga**

**Get all manga from database**

**Get manga by name and keyword**

**Get manga by genre**

**Get manga by id**

**Get manga by author name**

**Get manga by status**

**Create manga**

**Change manga information**

**Change manga cover image**

**Vote manga**

**Add chapter to manga**

**Change chapter information**

**Add image to manga**

**Delete chapter**

**3. Genre**

**Get all genre from database**

**Create new genre**

**Change genre name**

**Delete genre**

**4. Author**

**Get all author from database**

**Get authors by created user**

**Create new author**

**Change author name**

**Delete author**

]()