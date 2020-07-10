
# Project 4 - *Parstagram*

**Name of your app** is a photo sharing app using Parse as its backend.

Time spent: **22** hours spent in total

## User Stories

The following **required** functionality is completed:

- [X] User sees app icon in home screen.
- [X] User can sign up to create a new account using Parse authentication
- [X] User can log in and log out of his or her account
- [X] The current signed in user is persisted across app restarts
- [X] User can take a photo, add a caption, and post it to "Instagram"
- [X] User can view the last 20 posts submitted to "Instagram"
- [X] User can pull to refresh the last 20 posts submitted to "Instagram"
- [X] User can tap a post to view post details, including timestamp and caption.

The following **stretch** features are implemented:

- [X] Style the login page to look like the real Instagram login page.
- [X] Style the feed to look like the real Instagram feed.
- [X] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using fragments and a Bottom Navigation View.
- [X] User can load more posts once he or she reaches the bottom of the feed using endless scrolling.
- [X] Show the username and creation time for each post
- [X] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
  - [X] Allow the logged in user to add a profile photo
  - [X] Display the profile photo with each post
  - [X] Tapping on a post's username or profile photo goes to that user's profile page
  - [X] User Profile shows posts in a grid view
- [X] User can comment on a post and see all comments for each post in the post details screen.
- [X] User can like a post and see number of likes for each post in the post details screen.

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. I think I should have included the toolbar + menu that appears in the profile fragment in the mainActivity rather than the fragment itself as there is another instance where I use the same fragment but do not want the menu. I couldn't figure out how to get the toolbar to only show on the profile fragment in the mainActivity class, so I ended up passing in a boolean to the fragment to indicate whether or not the menu should be inflated, which doesn't seem the best.
2. I takes a while to unlike a photo in my app. I think it is b/c I make two query requests and one of them is destroying the object. I probably could find a different parse method that would allow me to just make one query request or I could possible add a boolean to the like object to see whether it is liked instead of adding/removing the object from the database every single time.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/hyang00/Parstagram/blob/master/Parstagram5.gif' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android
- [ScrollListener](https://gist.github.com/nesquena/d09dc68ff07e845cc622) - EndlessRecyclerViewScrollListener


## Notes

I couldn't figure how to allow the user to select an image from their camera gallery and upload it to parse. I also had some trouble coming up w/ how to implement the like feature. I'm also still unsure on passing data from fragments back to their activities. 

