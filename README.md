# HoodRobin - StockAlertsPaperTradingApp

## Table of Contents 
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Gifs](#Gifs)

## Overview

Group members:


[Ariq Zaman](https://github.com/ariqzaman) - Graphs and Charts

[Brian Nan](https://github.com/DogEnjoyer) - Paper Trading

[Isaac Anzures](https://github.com/ianzures) - News 

[Jimmy Wu](https://github.com/Jimmy-2) - Alerts/Notifications
### Description
Stock alerts and paper trading app for Hunter College CSCI Capstone course. Features news and graphs for stocks.



## Product Spec

### 1. Screen Archetypes

* Home/1st tab: Alerts
    * Users can search for a stock and add an price movement alert for that stock to the alerts list. The app will check every minute(1 min due to api limits) and if a stock reaches the price alert, the user will be notified through a push notification. Users can also manually swipe down to refresh the alert screens for updated stock data/prices. The completed alert will be automatically put into the complete alerts list with the time the alert was completed. Users can delete alerts from the alerts and completed alerts list as well as sort the lists by date added or by stock name. Clicking on an alert will lead the user to a time series candlestick chart for that alert's stock. Alerts and sort settings are persisted through SQLite database.
* 2nd tab: Paper Trading
    * Users can set a balance and start buying and selling stocks in our Paper trading game. The paper trading game includes achievements which are make 2 times your initial amount of money, have five times the intial amount of your money, have a million dollars in your balance, end up with 75% of your original balance and so on. The daily balance chart takes your current balance when you start the app for the first time in a day. The stock sector pie chart refreshes and displays the percentage of your portfolio that is of a certain sector. So if you were to have only technology stocks, your pie chart would be 100% techology. If you were to have 50% technology and 50% farming, your pie chart would reflect that.
* 3rd tab: News
    *  User can search for news pertaining to a stock or stocks which will then fill a recyclerview with articles/videos about that stock(s). The news item includes a title, abstract, sentiment about the article, a thumbnail, and list of associated stocks. The user can then click on a news item to have the associated link opened in their browser or video player. This tab also includes a button that takes the user to an "advanced options" screen, which allows them to save preferences for their search such as how the results will be sorted, which news sources to exclude from the results, how many results to show, what type of sentiment they want to see, and which type of news item (article/video/all) they want to see. 
* Graph Screen
    * Users can access a 1min, 5min or 10min candlestick chart of the stock's prices. The charts's data are taken from a time series api. Users can click on individual candlesticks to access relevant data for that candleestick such as the high, low, open and volume for that stick.

### 2. Navigation

**Bottom Navigation Bar** (3 Tabs - Users can navigate through the tabs by clicking on the navigation bar items) 
* Alerts Tab 
* Paper Trading Tab 
* News Tab
* Graph Screen - Accessed from Alerts and Papertrading screens.


**Flow Navigation** (Screen to Screen)

* Home/1st tab - Alerts Screen: - Users can navigate to the Add Alerts Screen by searching for a viable stock ticker. If the ticker does not exist, no navigation will occur. After successfully adding an alert, the user will be taken back to the Alerts home screen. The user can tap the back button if they do not wish to add an alert. Users can also navigate to the timeseries graph screen by clicking on an alert.
* 2nd tab - Paper Trading: Users can navigate to the timeseries graph screen by clicking on an alert. Users can also add and sell stocks by clicking on the trade button on each stock's row. This will lead to the trade screen.
* 3rd tab - News: Users can open up a browser version of the news selected.
* Graph Screen: Users can access a stock's timeseries price chart by clicking an alert from the Alerts screen or a stock from the Papertrading screen. Users can navigate back to the respective screens by clicking on the back button.



## Gifs (These gifs are old as the subscription for some of the APIs have ended)

##### Alerts screen:

<img src='https://github.com/Jimmy-2/HoodRobin/blob/master/gifs/AlertsScreen.gif?raw=true' title='Push Notifications' width='' alt='Push Notifications' />  <img src='https://github.com/Jimmy-2/HoodRobin/blob/master/gifs/AlertsScreen2.gif?raw=true' title='Data Persistence' width='' alt='Data Persistence' />


##### Portfolio screen and graphs:

<img src='https://github.com/Jimmy-2/HoodRobin/blob/master/gifs/PortfolioScreenAndGraphs1.gif?raw=true' title='Candlestick Screens' width='' alt='Candlestick Screens' /><img src='https://github.com/Jimmy-2/HoodRobin/blob/master/gifs/PortfolioScreenAndGraphs2.gif?raw=true' title='Portfolio Screen Graphs' width='' alt='Portfolio Screen Graphs' />


##### News screen:

<img src='https://github.com/Jimmy-2/HoodRobin/blob/master/gifs/NewsScreen.gif?raw=true' title='News Screen' width='' alt='News Screen' />

