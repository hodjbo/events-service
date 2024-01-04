# events-service

## User Controller API Documentation


This controller handles user-related operations such as user login and signup for the Events Service API.

#### Signup: POST /user/signup

Description: Registers a new user with a username and password.
Request Body:
{
"username": "newuser@example.com",
"password": "newpassword123"
}

Response:
* 200 OK: Successful signup. Returns an authentication token.
* 500 Internal Server Error: Failed to sign up the user.

To create a new user:

1. Make a POST request to /user/signup.
2. Provide the username and password in the request body.
3. If the signup is successful, you will receive an authentication token.


#### Login: POST /user/login
Description: Logs in a user using their username and password.
Request Body:
{
"username": "user@example.com",
"password": "password123"
}

Response:
* 200 OK: Successful login. Returns an authentication token.
* 401 Unauthorized: Incorrect username or password.

To authenticate a user:
1. Make a POST request to /user/login.
2. Provide the username and password in the request body.
3. If the login is successful, you will receive an authentication token.

## Event Controller API Documentation

#### Create an Event: POST /event/create
Create a new event.
Description: Request body should contain details of the event.

Request Body:
{
"name": "Example Event",
"venue": "Example Venue",
"description": "This is an example event.",
"maxParticipants": 100,
"date": "2023-12-31T18:00:00",
"location": "Example Location"
}

Response:
* 200 OK: Returns the event.
* 500 Internal Server Error.


#### Sign Up for an Event: POST /event/signup/{eventId}
Create a new event.
Description: Sign up a user for a specific event.

Request parameters:
* eventId: ID of the event to sign up for.
Headers:
Auth-Token: Authentication token for the user.
Example: /event/signup/123

Response: true/false


#### Get Event Details: GET /event/{eventId}
Description: Get details of a specific event.

Request parameters:
* eventId: ID of the event.
Example: /event/123

Response: 
* 200 OK: Returns the event.
* 500 Internal Server Error.


#### Update an Event: PUT /event
Description: Update details of an existing event.
Request body should contain updated details of the event.

{
"id": 123,
"name": "Updated Event Name",
"venue": "Updated Venue",
"description": "Updated description",
"maxParticipants": 150,
"date": "2023-12-31T18:00:00",
"location": "Updated Location"
}


#### Remove an Event: DELETE /event/{eventId}
Description: Remove an event by its ID.
Request parameters:
* eventId: ID of the event to be removed.
Example: /event/123


#### Get Scheduled Events: GET /event/scheduled-events
Description: Get a list of all scheduled events.


#### Get Events by Venue: GET /event/scheduled-events/venue/{venue}
Description: Get events by a specific venue.
Request parameters:
* venue: Name of the venue.
Headers:
Auth-Token: Authentication token for the user.
Example: /event/scheduled-events/venue/ExampleVenue
Description: Get a list of all scheduled events. an Event: DELETE /event/{eventId}


#### Get Events by Location: GET /event/scheduled-events/location/{location}
Description: Get events by a specific location.
Request parameters:
* location: Name of the location.
Headers:
Auth-Token: Authentication token for the user.
Example: /event/scheduled-events/venue/ExampleLocation


#### Get Sorted Events: GET /event/scheduled-events/sorted
Description: Get events sorted by a specific criterion: date, popularity, and creation-time.
Request parameters:
* orderBy: Sorting criterion (e.g., date, popularity, creation-time).
Headers:
Auth-Token: Authentication token for the user.
Example: /event/scheduled-events/sorted?orderBy=date
