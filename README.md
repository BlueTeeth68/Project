# Read manga online website

# Function list:
 - Register by Email, facebook or username and password
 - Reset password
 - Update profile (display name, avatar,...)
 - Notification
 - Search mangas by name, genre, author, translator,...
 - Sort mangas by views (day, week, month,...)
 - Suggest manga
 - Register for translator account
 - Register a translator group
 - Rate manga (user rate and translator rate (for suggest manga))
 - Report manga, user, translator
 - Add manga to favorite (bookmark)
 - View reading history, comment history
 - Comment, reply comment
 - User management: block, add role, send notification to user

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
1. User:
- Get all user list (admin) (Get "/api/admin/users")
- Get user by Id (admin) (Get "/api/admin/users/{id}")
- Get user by username (admin) (Get "/api/admin/users/{username}")
- Create user (admin) (Post "api/admin/users")
- Add/remove role to user (admin) 
- Deactivate/activate user (admin) (Patch "api/admin/users")
