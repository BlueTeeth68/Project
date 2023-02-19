# Read manga online website

# Function list:

- Register by Email, facebook or username and password
- Reset password
- Update profile (display name, avatar,...)
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

0. Authenticate and Authorize:

- Default admin account: username: SystemAdmin, password: 0000

0.1 Register new account:

- Require: none
- URL: /api/auth/register
- Method: POST
- Request param: UsernamePasswordVM (String username, String password)
- Response body: Token(String token)

0.2 Authorize account:

- Require: none
- URL: /api/auth/authenticate
- Method: POST
- Request param: UsernamePasswordVM (String username, String password)
- Response body: Token(String token)

1. User:

1.1 Get user list:

- Require: ADMIN
- URL: /api/admin/users
- Method: GET
- Request param: none
- Response body: List<User>

1.2 Get user by id:

- Require: ADMIN
- URL: /api/admin/users/{id}
- Method: GET
- Request param: none
- Response body: User

1.3 Get user by username:

- Require: ADMIN
- URL: /api/admin/users/username/{username}
- Method: GET
- Request param: none
- Response body: User

1.4 Get user by activate status:

- Require: ADMIN
- URL: /api/admin/users/activate/{activate}
- Method: GET
- Request param: none
- Response body: List<User>

1.5 Change user role

- Require: ADMIN
- URL: /api/admin/users/{id}/change-role
- Method: PATCH
- Request param: User (Long id, String role)
- Response body: User

1.6 Activate/Deactivate user

- Require: ADMIN
- URL: /api/admin/users/{id}/change-activate-status
- Method: PATCH
- Request param: User (Long id, Boolean activate)
- Response body: User

1.7 Get current user

- Require: authenticated account
- URL: /api/account/profile/{username}
- Method: GET
- Request param: None
- Response body: User

1.8 Update user information (displayName, avatar)

- Require: authenticated account
- URL: /api/account/profile/{username}
- Method: PATCH
- Request param: User (Long id, String displayName, byte[] avatar)
- Response body: User

1.7 Register account

1.8 Change password

1.9 Reset password

1.10 Link with Google account

1.11 Link with Facebook account

1.12 Remove Google account

1.13 Remove Facebook account

1.14 Change Google account

1.15 Change Facebook account





- Get user by Id (admin) (Get "/api/admin/users/{id}")
- Get user by username (admin) (Get "/api/admin/users/{username}")
- Create user (admin) (Post "api/admin/users")
- Add/remove role to user (admin)
- Deactivate/activate user (admin) (Patch "api/admin/users")
