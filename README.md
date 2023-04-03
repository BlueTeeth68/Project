# Read manga online website

# Swagger URL: 
http://localhost:8080/swagger-ui/index.html#/
# Swagger server URL:
http://readmangaonline-env.eba-44cmjrz9.ap-southeast-1.elasticbeanstalk.com/swagger-ui/index.html

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
- JUnit 5, Mockito

# Environment

- IDE: Intellij
- Maven
- jdk 17
- Database: MySQL
- Cloud: AWS

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
- Request param: size, page (size > 0, page >= 0)
- Request body: none
- Response body: PagingReturnDTO<User>
- Description: Admin can get all the user from the database. The result return relieve on the size and page in request (
  paging). Default size is 100, default page is 1.

#### 1.2 Get user by id/username: *

- Require: ADMIN
- URL: /admin/user
- Method: GET
- Request param: id/username
- Request body: none
- Response body: User or ResourceNotFoundException
- Description: Admin can get user by id or username.

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
- URL: /account
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
- Request body: file
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

#### Change password - ok

- Require: authenticated user
- URL: /account/password
- Method: PATCH
- Request param: none
- Request body: ChangPasswordVM(oldPassword, newPassword)
- Response body: User
- Description: user can change their current password

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
- Request param: size, page (size > 0, page >= 0)
- Request body: none
- Response body: PagingReturnDTO<SearchMangaDTO>
- Description: user can load all manga from the database, sorted by latest update (desc). The result return is based on
  the size and page on the request. The default value of size is 100, page is 1

#### Get mangas by name and keyword *

- Require: none
- URL: /manga/name
- Method: GET
- Request param: keyword, size, page (size > 0, page >= 0)
- Request body: none
- Response body: PagingReturnDTO<SearchMangaDTO>
- Description: user can find manga by its name or keyword, sorted by name. The result return is based on the size and
  page on the
  request. The default value of size is 20, page is 1

#### Get mangas by genre *

- Require: none
- URL: /manga/genre
- Method: GET
- Request param: id, size, page (size > 0, page >= 0)
- Request body: none
- Response body: PagingReturnDTO<SearchMangaDTO>
- Description: User can find manga by its genre, sorted by name. The result return is based on size and page on the
  request. Default id
  is 1, default size is 20, default page is 1

#### Get manga by id *

- Require: none
- URL: /manga
- Method: GET
- Request param: id
- Request body: none
- Response body: MangaDTO or ResourceNoFound exception
- Description: user can find manga by its id. Default value of id is 1.

#### Get mangas by author name *

- Require: none
- URL: /manga/author
- Method: GET
- Request param: id, size, page (size > 0, page >= 0)
- Request body: none
- Response body: PagingReturnDTO<SearchMangaDTO>
- Description: user can find manga by its author id, sorted by name. The result return is based on size and page on the
  request. Default id = 1, default size = 20, default page = 1

#### Get mangas by translator id *

- Require: none
- URL: /manga/translator
- Method: GET
- Request param: id, size, page (size > 0, page >= 0)
- Request body: none
- Response body: PagingReturnDTO<SearchMangaDTO>
- Description: user can find manga by its translator id, sorted by name. The result return is based on size and page on
  the request.
  Default id = 1, default size = 20, default page = 1

#### Get suggest manga (sort by rate * avg_view)

- Require: none
- URL: /manga/suggest
- Method: GET
- Request param: size, page (size > 0, page >= 0)
- Request body: none
- Response body: PagingReturnDTO<SearchMangaDTO>
- Description: Return all manga, order by (rate * view). The result return is based on size and page on the request.
  Default size = 20, default page = 1

#### Get mangas by status *

- Require: none
- URL: /manga/status
- Method: GET
- Request param: status (Ongoing/Completed), size, page (size > 0, page >= 0)
- Request body: none
- Response body: PagingReturnDTO<SearchMangaDTO>
- Description: user can find manga by status, sorted by name. Default size = 20, default page = 1

#### Create manga *

- Require: ADMIN or TRANSLATOR
- URL: /manga
- Method: POST
- Request param: none
- Request body: CreateMangaVM (name (not blank, min = 1, max = 256),
  summary (optional), yearOfPublication (not null, between 1900 and 2100))
- Response body: MangaDTO
- Description: translator or admin can create new manga

#### Update cover image *         

- Require: ADMIN or TRANSLATOR
- URL: /manga/cover-image
- Method: PATCH
- Request param: none
- Request body: id, MultipartFile file
- Response body: MangaDTO
- Description: translator or admin can update their own manga cover_image

#### Set genres to manga *        

- Require: ADMIN or TRANSLATOR
- URL: /manga/genre
- Method: PATCH
- Request param: none
- Request body: SetGenreToMangaVM (id: not null, Set/list genreName: not null)
- Response body: MangaDTO
- Description: translator or admin can set genres for their own manga

#### Set authors to manga *  

- Require: ADMIN or TRANSLATOR
- URL: /manga/author
- Method: PATCH
- Request param: none
- Request body: SetAuthorsToMangaVM (id: not null, Set/List authorId: not null)
- Response body: MangaDTO
- Description: translator or admin can set authors for their own manga

#### Set keyword to manga *

- Require: ADMIN or TRANSLATOR
- URL: /manga/keyword
- Method: PATCH
- Request param: none
- Request body: KeywordMangaVM (mangaId, List<String> keyword)
- Response body: MangaDTO
- Description: translator or admin can set authors for their own manga

#### Change manga information *

- Require: ADMIN or TRANSLATOR
- URL: /manga
- Method: PATCH
- Request param: none
- Request body: ChangeMangaVM(id, name, summary, status, yearOfPublication)
- Response body: MangaDTO
- Description: translator or admin change their manga information

#### Vote manga *

- Require: authenticated user
- URL: /manga/rate
- Method: PATCH
- Request param: none
- Request body: RateVM (point, mangaId)
- Response body: RateVM
- Description: authenticated user can rate the manga

#### Get chapter by id *

- Require: none
- URL: /manga/chapter/{id}
- Method: GET
- Request param: none
- Request body: none
- Response body: ChapterImageDTO (chapterDTO and its image's urls)
- Description: User can find chapter by its id. The result is common information of chapter and its chapter image's
  urls.

#### Add chapter to manga *

- Require: ADMIN or TRANSLATOR
- URL: /manga/chapter
- Method: POST
- Request param: none
- Request body: CreateChapterVM
- Response body: MangaDTO
- Description: ADMIN user or TRANSLATOR (who create this manga) can add new chapter to manga. The latest update of manga
  will be updated.

#### Change chapter information *

- Require: ADMIN or TRANSLATOR
- URL: /manga/chapter
- Method: PATCH
- Request param: none
- Request body: UpdateChapterVM
- Response body: MangaDTO
- Description: ADMIN user or TRANSLATOR (who create this manga) can update information of chapter

#### Add images to manga chapter *

- Require: ADMIN or TRANSLATOR
- URL: manga/chapter/chapter-images/{chapterId}
- Method: POST
- Request param: MultipartFile[] files
- Request body: none
- Response body: MangaDTO
- Description: ADMIN user or TRANSLATOR (who create this manga) can add chapter's images to chapter.

#### Delete chapter *

- Require: ADMIN or TRANSLATOR
- URL: /manga/chapter
- Method: DELETE
- Request param: id
- Request body: none
- Response body: void
- Description: ADMIN user or TRANSLATOR (who create this manga) can delete a chapter.

#### Delete Manga *

- Require: ADMIN or TRANSLATOR
- URL: /manga/{id}
- Method: DELETE
- Request param: none
- Request body: none
- Response body: void
- Description: ADMIN user or TRANSLATOR (who create this manga) can delete a manga.

#### Add manga to bookmark *

- Require: authenticated user
- URL: /bookmark/{mangaId}
- Method: POST
- Request param: none
- Request body: none
- Response body: void
- Description: user can add manga to bookmark. If manga already exists, then remove bookmark.

#### Get all bookmark of user *

- Require: authenticated user
- URL: /bookmark
- Method: Get
- Request param: none
- Request body: none
- Response body: List<BookmarkDTO>
- Description: user can get all their bookmark sorted by id desc. Each bookmarkDTO has common information of manga

#### Report manga

### 3. Genre

#### Get all genre from database *

- Require: none
- URL: /genre/list
- Method: GET
- Request param: size, page (size > 0, page >= 0)
- Request body: none
- Response body: PagingReturnDTO<Genre>
- Description: user can get all genre from the database. The result return is based on size and page on the request.
  Default size is 50, default page is 1

#### Get genre by name *

- Require: none
- URL: /genre/name
- Method: GET
- Request param: name
- Request body: none
- Response body: List<Genre>
- Description: User can find genre by its name.

#### Get genre by id *

- Require: none
- URL: /genre/id
- Method: GET
- Request param: id
- Request body: none
- Response body: Genre or ResourceNotFound exception
- Description: User can find genre by its id.

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
- Request body: Genre genre (id, name)
- Response body: Genre
- Description: Admin user can change the name of genre

#### Delete genre *

- Require: ADMIN
- URL: /genre
- Method: DELETE
- Request param: id
- Request body: none
- Response body: none
- Description: Admin can delete a genre

### 4. Author

#### Get all author from database *

- Require: ADMIN or TRANSLATOR
- URL: /author/list
- Method: GET
- Request param: size, page (size > 0, page >= 0)
- Request body: none
- Response body: PagingReturnDTO<Author>
- Description: ADMIN or TRANSLATOR can get all author list from database. The result return is based on size and page on
  the request. Default size is 50, default page is 1

#### Get author by id or a part of name *

- Require: ADMIN or TRANSLATOR
- URL: /author
- Method: GET
- Request param: id/ name
- Request body: none
- Response body: List<Author>
- Description: User can find author by id or author name

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

#### Add keyword to a manga *

- Require: none
- URL: /keyword
- Method: POST
- Request param: none
- Request body: KeywordDTO (name, mangaId)
- Response body: Keyword
- Description: Admin or translator can add a keyword to a manga

#### Change keyword name *

- Require: none
- URL: /keyword
- Method: PATCH
- Request param: none
- Request body: ChangeKeywordVM (name, mangaId, newName)
- Response body: Keyword
- Description: Admin or translator can change name of a keyword in a manga

#### Delete keyword *

- Require: none
- URL: /keyword
- Method: DELETE
- Request param: name, mangaId
- Request body: none
- Response body: none
- Description: Admin or translator can remove a keyword from a manga

### 6.Comment

#### Get all comment and reply comment of

- Require: none
- URL: /manga/comment
- Method: GET
- Request param: mangaId, page, size
- Request body: none
- Response body: PagingReturnDTO<CommentDTO>
- Description: user can get all comment and reply comment in a manga.

#### Comment on manga

- Require: logged-in user
- URL: /manga/comment
- Method: POST
- Request param: page, size
- Request body: CreateCommentVM
- Response body: PagingReturnDTO<CommentDTO>
- Description: logged-in user can comment on a manga

#### Change comment content

- Require: logged-in user
- URL: /manga/comment
- Method: PATCH
- Request param: none
- Request body: ChangeCommentVM
- Response body: CommentDTO
- Description: logged-in user can change their own comment content

#### Delete comment

- Require: logged-in user
- URL: /manga/comment
- Method: PATCH
- Request param: id
- Request body: none
- Response body: none
- Description: logged-in user can delete their own comment. Comment that is deleted is change status to deleted.

#### Reply comment

- Require: logged-in user
- URL: /manga/comment/reply
- Method: POST
- Request param: page, size
- Request body: CreateCommentVM
- Response body: PagingReturnDTO<ReplyCommentDTO>
- Description: logged-in user can reply a comment on a manga.

#### Change reply comment

- Require: logged-in user
- URL: /manga/comment/reply
- Method: PATCH
- Request param:
- Request body: ChangeCommentVM
- Response body: ReplyCommentDTO
- Description: logged-in user can change their own reply comment

#### Delete reply comment

- Require: logged-in user
- URL: /manga/comment/reply
- Method: PATCH
- Request param: id
- Request body: none
- Response body: none
- Description: logged-in user can delete their own reply comment. Comment that is deleted is change status to deleted.

#### Report comment

### 7.Notification

#### Get all user's notifications sorted by date desc

#### Create notification (auto)

#### Change status of notification (read/ not read)

#### Delete notification

### 8.Report
