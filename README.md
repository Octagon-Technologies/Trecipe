# Trecipe
A beautifully designed recipe app created with XML for the UI and Kotlin for the logic.

<img width="920" alt="play_store_graphic (1)" src="https://github.com/Octagon-Technologies/Trecipe/assets/62815445/36893d5a-3373-421e-8206-1e031417bf38">

## Setup
1) Fork the repository
2) Register API key on Spoonacular API.
3) Create a new propeties file `api_keys.properties`
4) Add the recipe key as: `RECIPE_API_KEY = key` in `api_keys.properties`
   
   ```
        val apiKeysFile = project.rootProject.file("api_keys.properties")
        val properties = Properties()
        properties.load(apiKeysFile.inputStream())

        val recipeApiKey = properties.getProperty("RECIPE_API_KEY")
        buildConfigField("String", "RECIPE_API_KEY", recipeApiKey)
   ```
6) Sync and build project

## App Screenshots
<img src="https://github.com/Octagon-Technologies/Trecipe/assets/62815445/632b925d-d561-44cc-80d2-42949362a6f2" width="250">
<img src="https://github.com/Octagon-Technologies/Trecipe/assets/62815445/4d4f7277-995f-4cd3-9994-19691d91868c" width="250">
<img src="https://github.com/Octagon-Technologies/Trecipe/assets/62815445/5f3a31ef-8543-4da7-b6e7-3dfb5c4d5e5f" width="250">
<img src="https://github.com/Octagon-Technologies/Trecipe/assets/62815445/6a99159e-3575-4e4b-8381-c520f614d874" width="250">


## Technologies used
- Kotlin
- Retrofit for network calls
- Moshi for json deserialization
- Room for local caching of weather data


 ## Issues and TO-FIX items
 - Fix the Saved and Like button functionality
 - Improve `Search` functionality
