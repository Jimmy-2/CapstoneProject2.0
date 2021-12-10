# HoodRobin - StockAlertsPaperTradingApp

## Table of Contents (to be added)
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Gifs](#Gifs)

## Overview

Group members:


[Ariq Zaman](https://github.com/ariqzaman) - Graphs

[Brian Nan](https://github.com/DogEnjoyer) - Papertrading

[Isaac Anzures](https://github.com/ianzures) - News 

[Jimmy Wu](https://github.com/Jimmy-2) - Alerts/Notifications
### Description
Stock alerts and papertrading app for Hunter College CSCI Capstone course. Features news and graphs for stocks.



## Product Spec

### 1. Screen Archetypes

* Home/1st tab: Alerts
    * Users can search for a stock and add an price movement alert for that stock to the alerts list. The app will check every minute(1 min due to api limits) and if a stock reaches the price alert, the user will be notified through a push notification. The completed alert will be automatically put into the complete alerts list with the time the alert was completed. Users can delete alerts from the alerts and completed alerts list as well as sort the lists by date added or by stock name. Clicking on an alert will lead the user to a time series candlestick chart for that alert's stock.    

### 2. Navigation

**Bottom Navigation Bar** (3 Tabs) 
* Alerts Tab
* Paper Trading Tab
* News Tab
     * User can search for news pertaining to a stock or stocks which will then fill a recyclerview with articles/videos about that stock(s). The news item includes a title, abstract, sentiment about the article, a thumbnail, and list of associated stocks. The user can then click on a news item to have the associated link opened in their browser or video player. This tab also includes a button that takes the user to an "advanced options" screen, which allows them to save preferences for their search such as how the results will be sorted, which news sources to exclude from the results, how many results to show, what type of sentiment they want to see, and which type of news item (article/video/all) they want to see. 

**Flow Navigation** (Screen to Screen)

* Home/1st tab:
