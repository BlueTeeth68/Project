# Read manga online website

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
- Response body: List<User>
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
- Request param: none
- Request body: ChangeUserRoleVM (id, role (USER, ADMIN, TRANSLATOR))
- Response body: User or BadRequestException
- Description: Admin can change role of a user

#### Activate/Deactivate user: *

- Require: ADMIN
- URL: /admin/user/active-status
- Method: PATCH
- Request param: none
- Request body: ChangeUserStatusVM (id, status (true/false))
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
- Request param: none
- Request body: displayName
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

#### Get limit (paginate) manga from database *

- Require: none
- URL: /manga/list
- Method: GET
- Request param: limit, page (limit > 0, page >= 1)
- Request body: none
- Response body: List<MangaDTO>
- Description: user can load limit number of manga, sorted by latest update (paginate). If limit or page is null
  return the first 100 manga from database.

#### Get mangas by name and keyword *

- Require: none
- URL: /manga/name
- Method: GET
- Request param: keyword, optional: limit, page (limit > 0, page >= 1)
- Request body:
- Response body: List<MangaDTO>
- Description: user can find limit (paginate) mangas by keyword/name. Default limit = 20, default page = 1

#### Get mangas by genre *

- Require: none
- URL: /manga/genre
- Method: GET
- Request param: id, optional: limit, page (limit > 0, page >= 1)
- Request body:
- Response body: List<MangaDTO>
- Description: user can find limit (paginate) mangas by its genre. Default limit = 20, default page = 1

#### Get manga by id *

- Require: none
- URL: /manga
- Method: GET
- Request param: id
- Request body: none
- Response body: MangaDTO
- Description: user can find manga by its id

#### Get mangas by author name *

- Require: none
- URL: /manga/author
- Method: GET
- Request param: id, optional: limit, page (limit > 0, page >= 1)
- Request body: none
- Response body: List<MangaDTO>
- Description: user can find limit (paginate) mangas by its authorId. Default limit = 20, default page = 1

#### Get mangas by translator id -doing

- Require: none
- URL: /manga/translator
- Method: GET
- Request param: id, optional: limit, page (limit > 0, page >= 1)
- Request body: none
- Response body: List<MangaDTO>
- Description: user can find limit (paginate) mangas by its translator (create user). Default limit = 20, default page =
  1

#### Get suggest manga (sort by rate * avg_view)

- Require: none
- URL: /manga/suggest
- Method: GET
- Request param: optional: limit, page (limit > 0, page >= 1)
- Request body: none
- Response body: List<MangaDTO>
- Description: user can find limit (paginate) mangas, order by (rate * view). Default limit = 20, default page = 1

#### Get mangas by status

- Require: none
- URL: /manga/status
- Method: GET
- Request param: status (Ongoing/Completed), optional: limit, page (limit > 0, page >= 1)
- Request body: none
- Response body: List<MangaDTO>
- Description: user can find limit (paginate) mangas by status sorted. Default limit = 20, default page = 1

#### Create manga *

- Require: ADMIN or TRANSLATOR
- URL: /manga
- Method: POST
- Request param: none
- Request body: CreateMangaVM (name (not blank, min = 1, max = 256),
  summary (optional), yearOfPublication (not null, between 1900 and 2100))
- Response body: Manga
- Description: translator or admin can create new manga

#### Update cover image *         3

- Require: ADMIN or TRANSLATOR
- URL: /manga/cover-image
- Method: PATCH
- Request param: none
- Request body: id, MultipartFile file
- Response body: Manga
- Description: translator or admin can update their own manga cover_image

#### Set genres to manga *        1

- Require: ADMIN or TRANSLATOR
- URL: /manga/genre
- Method: PATCH
- Request param: none
- Request body: SetGenreToMangaVM (id: not null, Set/list genreName: not null)
- Response body: Manga
- Description: translator or admin can set genres for their own manga

#### Set authors to manga *       2

- Require: none
- URL: /manga/author
- Method: PATCH
- Request param: none
- Request body: SetAuthorsToMangaVM (id: not null, Set/List authorId: not null)
- Response body: Manga
- Description: translator or admin can set authors for their own manga

#### Add keyword to manga - doing

#### Remove keyword to manga - doing

#### Change manga information

#### Change manga cover image - doing

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
- Response body: List<Genre>
- Description: user can load a limit genre form database (paginate) or load the first 50 genre by set
  limit or page to null

#### Get genre by name or id *

- Require: none
- URL: /genre
- Method: GET
- Request param: id/name (depend on the purpose but just send 1 in 2)
- Request body: none
- Response body: List<Genre>
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

#### Get all author from database - ok

- Require: ADMIN or TRANSLATOR
- URL: /author/list
- Method: GET
- Request param: limit, page (limit > 0, page >= 1)
- Request body: none
- Response body: List<Author>
- Description: User can find a limit author (paginate). Leave limit empty to retrieve the first 50 Author

#### Get author by id or a part of name *

- Require: ADMIN or TRANSLATOR
- URL: /author
- Method: GET
- Request param: id/ name
- Request body: none
- Response body: List<Author>
- Description: User can find author by id or author name (using LIKE (%name%))

#### Get authors by created user *

- Require: ADMIN or TRANSLATOR
- URL: /author/created-by
- Method: GET
- Request param: userId
- Request body: none
- Response body: List<Author>
- Description: User can find author by user's id who created this author

#### Create new author *

- Require: ADMIN, TRANSLATOR
- URL: /author
- Method: POST
- Request param: none
- Request body: name
- Response body: Author
- Description: Admin user can create new Genre

#### Change author name *

- Require: ADMIN, TRANSLATOR
- URL: /author
- Method: PATCH
- Request param: id, name
- Request body: none
- Response body: Author
- Description: User can change author's name that they have created (except admin)

#### Delete author *

- Require: ADMIN, TRANSLATOR
- URL: /author
- Method: DELETE
- Request param: id
- Request body: none
- Response body: none
- Description: User can delete author's name that they have created (except admin)

### 5.Keyword

#### Get all keyword of manga sort by name *

- Require: none
- URL: /keyword/manga
- Method: GET
- Request param: mangaId
- Request body: none
- Response body: List<Keyword>
- Description: User can search all keyword of a manga

#### Get keyword by name and mangaId *

- Require: none
- URL: /keyword
- Method: GET
- Request param: name, mangaId
- Request body: none
- Response body: Keyword
- Description: User can search keyword by keyword's name and mangaId

#### Add keyword to a manga -  *

- Require: none
- URL: /keyword
- Method: POST
- Request param: none
- Request body: KeywordDTO (name, mangaId)
- Response body: Keyword
- Description: Admin or translator can add a keyword to a manga

#### Change keyword name -  *

- Require: none
- URL: /keyword
- Method: PATCH
- Request param: none
- Request body: ChangeKeywordVM (name, mangaId, newName)
- Response body: Keyword
- Description: Admin or translator can change name of a keyword in a manga

#### Delete keyword - * testing

- Require: none
- URL: /keyword
- Method: DELETE
- Request param: name, mangaId
- Request body: none
- Response body: none
- Description: Admin or translator can remove a keyword from a manga

### 6.Comment

#### Get all comment and reply comment of manga

#### Comment on manga

#### Change comment content

#### Delete comment

#### Reply comment

#### Change reply comment

#### Report comment

### 7.Notification

### 8.Report