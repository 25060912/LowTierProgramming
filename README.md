## Low Tier Programming

Repository for Low Tier Programming's WIX1002 (Fundamentals of Programming) Semester 1 25/26 Project.

CREATE YOUR OWN BRANCH (DO THIS IF THIS IS YOUR FIRST TIME EDITING A CODE):
1. git clone repoURLhere
2. git checkout -b yourname-feature

Workflow when you want to edit your code:
DO this inside your command prompt. 
Change directory to the project folder
1. git checkout main
2. git pull origin main
3. git checkout yourname-feature
4. Edit stuffs you want, once you're done do the steps below:
5. git add fileNamehere
6. git commit -m "comment  here"
7. git push origin yourname-feature
8. Go to the GitHub repo
9. Click Pull Request
10. Merge pull request

CREATE YOUR OWN .ENV FILE:
If you want to run the codes that are related to the AI calls, you need an API key. 
- Go to https://aistudio.google.com/api-keys
- Create API key (top right)
- Name it whatever, choose "Default Gemini Project" as imported project
- Copy the key
- Create a new file inside the data folder named ".env" without the ""
- Type `GEMINI_TOKEN=yourtokenhere` inside the file

IMPORTANT !! 
DO NOT ADD OR COMMIT YOUR .ENV FILE TO THE REPOSITORY!!