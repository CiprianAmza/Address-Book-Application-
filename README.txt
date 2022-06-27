Short presentation of the AddressBook application.

My approach had two parts.

Backend part:
    - I created an application-server with 7 basic pages (html files).
    If the user is not logged in, he is able to access the `/home` page (where he can check all the information of the address book),
    is able to search users (`/searchByUserName`), to export all the information in a CSV-file and to login (`/login`) or to register(`/createAccount`).

    If the user is logged in, he is able to see the Add New User, Edit and Delete buttons in the Administration Panel ('/hello') and to log out.

    - adding a user requires all the 4 fields (firstName, lastName, address and a link to an image), but any of these fields may be empty.
    - editing a user offers the possibility to change all the values or only some values. If one value is not modified, it will remain unchanged.
    - deleting a user will delete him permanently
    - all the above changes will also occur in the database
    - exporting the CSV file will update any new modification

    - a user may search an address book person by first name or by last name or by any part of both of the names (searching `ian` will go to `Ciprian` for example)

    - a user may log in with some valid data and may create a new account if the username and the password are valid.
    - in order to register a new account, the username must not be already contained in the database, must be at least 6 characters length
    - the password must be at least 9 characters length, must contain at least 1 upper case, 1 lower case and 1 numerical character.
    - There is a problem here, because after registration, the pair (userName, password) is stored in the database, but it requires one re-run of the application,
    in order to be able to log in with the new data ( I couldn't solve it yet).


Frontend part:
    - I used bootstrap for many aspects of the application (especially for the buttons and text-fonts)
    - I used css and html for displaying the elements of the page
    - I decided to respect the (60-30-10) rule of colors, and I took the colors from the `https://coolors.co/palettes/trending`
    - I decided to use the same background color for all the pages, so that the user has the impression of a single web-page
    - I used JavaScript in the `/hello` <script> part, in order to modify the colors every 3 seconds.
    - I also displayed the images of the links stored at each user (user.getPhoto())

