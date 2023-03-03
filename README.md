_# Read manga online website

# Function list:

- Register by Email, facebook or username and password
- Reset password
- Update profile (display name, avatar, password)
- Manage user (change role, activate, deactivate)
- Notification
- Search mangas by name, genre, author, translator, keyword
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

### 0. Authenticate and Authorize:

- Default account:

+ admin: username: SystemAdmin, password: 0000
+ user: username: user, password: 0000

#### 0.1 Register new account: *

- Require: none
- URL: /api/auth/register
- Method: POST
- Request param: none
- Request body: UsernamePasswordVM (String username, String password)
- Response body: Token(String token)
- Description: User can register new account by providing username and password

#### 0.2 Authorize account: *

- Require: none
- URL: /api/auth/authenticate
- Method: POST
- Request param: none
- Request body: UsernamePasswordVM (String username, String password)
- Response body: Token(String token)
- Description: User can log in to the system by using username and password

### 1. User:

#### 1.1 Get user list (with paginate): *

- Require: ADMIN
- URL: /admin/user/list
- Method: GET
- Request param: limit, page (limit > 0, page >= 1)
- Request body: none
- Response body: List<<User>>
- Description: Admin can get limit user from database (paginate). If limit or page is null, return all user.

#### 1.2 Get user by id/username: *

- Require: ADMIN
- URL: /admin/user
- Method: GET
- Request param: id/username
- Request body: none
- Response body: User or ResourceNotFoundException
- Description: Admin can get user by id or username. The response is User with almost properties.

#### Change user role: *

- Require: ADMIN
- URL: /admin/user/role
- Method: PATCH
- Request param: id, role (USER, ADMIN, TRANSLATOR)
- Request body: none
- Response body: User or BadRequestException
- Description: Admin can change role of a user

#### Activate/Deactivate user: *

- Require: ADMIN
- URL: /admin/user/active-status
- Method: PATCH
- Request param: id, status (true/false)
- Request body: none
- Response body: User or BadRequestException
- Description: Admin can activate or deactivate a user. User is deactivated can not log in to website

#### Get current user: *

- Require: authenticated account
- URL: /account
- Method: GET
- Request param: None
- Request body: none
- Response body: User
- Description: Return the user is logging into the website

#### Update displayName: *

- Require: authenticated account
- URL: /account/display-name
- Method: PATCH
- Request param: displayName
- Request body: none
- Response body: User or DataAlreadyExistsException
- Description: user can change display name that has not been existed in the system

#### Update avatar: *

- Require: authenticated user
- URL: /account/avatar
- Method: POST
- Request param: none
- Request body: file image
- Response body: User
- Description: user can update avatar by providing an image

#### Get user by id/username: *

- Require: authenticated user
- URL: /account/user
- Method: GET
- Request param: id/username
- Request body: none
- Response body: CommonUserDTO or ResourceNotFoundException
- Description: user can view others profile with public information

#### Change password

#### Reset password

#### Link with Google account

#### Link with Facebook account

#### Remove Google account

#### Remove Facebook account

#### Change Google account

#### Change Facebook account

#### Delete account: (Testing with other table)

- Require: authenticated user
- URL: /account
- Method: DELETE
- Request param: none
- Request body: none
- Response body: no content
- Description: user can delete their account from system

#### View reading history

#### Create notification

#### Get notification

### 2. Manga

#### Get all manga from database *

- Require: none
- URL: /manga/list
- Method: GET
- Request param: limit, page (limit > 0, page >= 1)
- Request body: none
- Response body: List<<Manga>>
- Description: user can load limit number of manga, sorted by latest update (paginate). If limit or page is null
  return the first 1000 manga from database.

#### Get mangas by name and keyword

#### Get mangas by genre

#### Get manga by id

#### Get mangas by author name

#### Get mangas by translator name

#### Sort manga by view

#### Sort manga by lasted update

#### Sort manga by rate

#### Suggest manga (sort by rate * view)

#### Get mangas by status

#### Create manga

#### Change manga information

#### Change manga cover image

#### Vote manga

#### Add chapter to manga

#### Change chapter information

#### Add images to manga chapter

#### Delete chapter

#### Delete Manga

#### Report manga

#### Add manga to bookmark

### 3. Genre

#### Get all genre from database *

- Require: none
- URL: /genre/list
- Method: GET
- Request param: limit, page (optional) (limit > 0, page >=1)
- Request body: none
- Response body: List<<Genre>>
- Description: user can load a limit genre form database (paginate) or load the first 50 genre by set
  limit or page to null

#### Get genre by name or id *

- Require: none
- URL: /genre
- Method: GET
- Request param: id/name (depend on the purpose but just send 1 in 2)
- Request body: none
- Response body: List<<Genre>>
- Description: User can find genre by id or genre name

#### Create new genre *

- Require: ADMIN
- URL: /genre
- Method: POST
- Request param: none
- Request body: Genre genre (can not have id)
- Response body: Genre
- Description: Admin user can add a new genre to database

#### Change genre name *

- Require: ADMIN
- URL: /genre
- Method: PATCH
- Request param: none
- Request body: Genre genre (id, name,...)
- Response body: Genre
- Description: Admin user can change the name of genre

#### Delete genre (Testing)

- Require: ADMIN
- URL: /genre
- Method: DELETE
- Request param: id
- Request body: none
- Response body: none
- Description: Admin can delete a genre

### 4. Author

#### Get all author from database *

- Require: none
- URL: /author/list
- Method: GET
- Request param: limit, page (limit > 0, page >= 1)
- Request body: none
- Response body: List<<Author>>
- Description: User can find a limit author (paginate). Leave limit empty to retrieve the first 50 Author

#### Get author by id or a part of name *

- Require: none
- URL: /author
- Method: GET
- Request param: id/ name 
- Request body: none
- Response body: List<<Author>>
- Description: User can find author by id or author name (using LIKE (%name%))

#### Get authors by created user *

- Require: none
- URL: /author/created-by
- Method: GET
- Request param: userId
- Request body: none
- Response body: List<<Author>>
- Description: User can find author by user's id who created this author

#### Create new author *

- Require: ADMIN, TRANSLATOR
- URL: /author
- Method: POST
- Request param: none
- Request body: Author (name)
- Response body: Author
- Description: Admin user can create new Genre 

#### Change author name  *

- Require: ADMIN, TRANSLATOR
- URL: /author
- Method: PATCH
- Request param: id, name
- Request body: none
- Response body: Author
- Description: User can change author's name that they have created (except admin)

#### Delete author  *

- Require: ADMIN, TRANSLATOR
- URL: /author
- Method: DELETE
- Request param: id
- Request body: none
- Response body: none
- Description: User can delete author's name that they have created (except admin)

### 5.Key word

#### Get all key word of manga - doing

#### Add keyword to a manga - doing

#### Change keyword name - doing

#### Delete keyword - doing

### 6.Comment

#### Get all comment and reply comment of manga

#### Comment on manga

#### Change comment content

#### Delete comment

#### Reply comment

#### Change reply comment

#### Report comment