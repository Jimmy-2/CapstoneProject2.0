# HoodRobin - StockAlertsPaperTradingApp

## Table of Contents (to be added)
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Gifs](#Gifs)

## Overview

Group members:


[Ariq Zaman](https://github.com/ariqzaman) - Graphs and Charts

[Brian Nan](https://github.com/DogEnjoyer) - Papertrading

[Isaac Anzures](https://github.com/ianzures) - News 

[Jimmy Wu](https://github.com/Jimmy-2) - Alerts/Notifications
### Description
Stock alerts and papertrading app for Hunter College CSCI Capstone course. Features news and graphs for stocks.



## Product Spec

### 1. Screen Archetypes

* Home/1st tab: Alerts
    * Users can search for a stock and add an price movement alert for that stock to the alerts list. The app will check every minute(1 min due to api limits) and if a stock reaches the price alert, the user will be notified through a push notification. The completed alert will be automatically put into the complete alerts list with the time the alert was completed. Users can delete alerts from the alerts and completed alerts list as well as sort the lists by date added or by stock name. Clicking on an alert will lead the user to a time series candlestick chart for that alert's stock. Alerts and sort settings are persisted through SQLite database.
* 2nd tab: Papertrading
    * 
* 3rd tab: News
    * 
* Graph Screen
    * 

### 2. Navigation

**Bottom Navigation Bar** (3 Tabs - Users can navigate through the tabs by clicking on the navigation bar items) 
* Alerts Tab 
* Paper Trading Tab 
* News Tab


**Flow Navigation** (Screen to Screen)

* Home/1st tab - Alerts Screen: - Navigate to the Add Alerts Screen by searching for a viable stock ticker. If the ticker does not exist, no navigation will occur. After successfully adding an alert, the user will be taken back to the Alerts home screen. The user can also hap the back button if they do not wish to add an alert. Users can also navigate to the timeseries graph screen by clicking on an alert.
* 2nd tab - Papertrading - Navigate to the timeseries graph screen by clicking on an alert.
* 3rd tab - News:
