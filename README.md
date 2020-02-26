# GuessTheCelebrity
Game of guessing celebrity's name:
- Download web content in html format from a website listing all celebrities. We use class DownloadWebContent.
- Download image of a celebrity on the list
- Use split() in string, Pattern.compile().matcher() to group image URLs and celebrity names in new ArrayLists
- Use Random to choose a random celebrity image and four different answers of celebrity names as texts on four buttons
- Add onClick() on each button to see if the chosen answer is correct or wrong
