## Inspiration
Buying a home is one of the largest decisions an individual will make in their lives. However, in recent years the housing market has become increasingly unpredictable and out of reach for individuals, not to mention the perceived unapproachability of a real estate agent to an average first-time home buyer. In the age of information, we saw an opportunity to make the real estate market more attainable for individuals. 

## What it does
Our project asks users to tap a location on an interactive map along with a few other key property characteristics (list price, number of bedrooms above/below ground and number of bathrooms) and predicts the true value of the property. 

## How we built it
This project is built using a combination of Java (Android Studio) for frontend and Python for backend. The frontend was created via integrated Google cloud services to create an interactive map GUI. The backend uses pandas for data extraction and manipulation, and sklearn to create and train a random forest regressor model to predict the prices based on our training set. This model was chosen as it stuck a good balance between accuracy, generalization and feasibility. 

## Challenges we ran into/What we learned 
One thing we learned for frontend was learning how the Google Map's API works for the interactive map GUI; figuring out how to generate API keys and integrating that into our code was the biggest challenge. 
We learned about two new concepts over backend development: web scraping and machine learning (random forest, to be specific). To run our core backend we created a random forest model (which was new to all of us), and although it wasn't fully implemented due to time constraints, we also made a rudimentary web scraper which could potentially increase the utility of our tool and create a better user interface. 

## What we're proud of
One of our biggest successes is training a model with an r-squared value of >0.99 and having a MSE of only 9,000,000. Although that seems like a large number, within the context of the data, this actually represents a model that is quite accurate (square root of this MSE value yields about $3000 fluctuation in the property price which is quite miniscule).

## What's next for RealTor
The next-most step for RealTor would be expanding our training data set for stronger and generalizable predictions using more features. Additionally, we hope to expand on our application as a one-stop shop for on-demand real-estate advice for buyers; the users can input their desired location along with key property traits (whether it's a single-family home, price range, number of bedrooms/bathrooms, etc.), and using web scraping to output a homes that match their criteria. 
