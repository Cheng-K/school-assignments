# ----------------------- Install necessary packages ----------------------- 
install.packages("ggplot2")
install.packages("dplyr")
install.packages("ggthemes")
install.packages("RColorBrewer")
install.packages("PerformanceAnalytics")
install.packages("plotly")
install.packages("DataExplorer")

# -------------------------- Load Libraries -------------------------------- 
library(ggplot2)
library(dplyr)
library(ggthemes)
library(RColorBrewer)
library(PerformanceAnalytics) # cancel out this
library(plotly)
library(DataExplorer)

# -------------------------- Data import ----------------------------------- 

# Change working directory
setwd("D:\\School Materials\\Year 2\\Programming For Data Analysis\\Assignment")

weatherData = read.csv("weather.csv")

# -------------------------- Data-Preprocessing ---------------------------- 

#Determine the dimensions of the weatherData
nrow(weatherData)
ncol(weatherData)

# Getting initial view on weatherData
View(head(weatherData))
View(tail(weatherData))

# Getting to know the data types for each column
str(weatherData)

# Get a report on the weather dataset (using DataExplorer)
View(introduce(weatherData))

#Plotting the initial overview of weatherDataset using DataExplorer
plot_intro(weatherData)

# Convert character values into factor objects
weatherData$WindGustDir = factor(weatherData$WindGustDir)
weatherData$WindDir9am = factor(weatherData$WindDir9am)
weatherData$WindDir3pm = factor(weatherData$WindDir3pm)
weatherData$RainToday = factor(weatherData$RainToday,levels = c("Yes","No"))
weatherData$RainTomorrow = factor(weatherData$RainTomorrow,levels = c("Yes","No"))


#Check for columns with missing value
summary(weatherData)

# Columns with missing value : (Total missing value 47)
#   1. Sunshine      (3 Missing value)
#   2. WindGustDir   (3 Missing value)
#   3. WindGustSpeed (2 Missing value)
#   4. WindDir9am    (31 Missing value)
#   5. WindDir3pm    (1 Missing value)
#   6. WindSpeed9am  (7 Missing value)


# Plot the missing data value percentage for each column
plot_missing(weatherData)


#Conclusion : 
#   Since there are very little missing values (<10%), treatment is not necessary.


# Renaming some columns 
names(weatherData)[6:19] = c("WindGust_Direction",
                             "WindGust_Speed",
                             "WindDirection_9am",
                             "WindDirection_3pm",
                             "WindSpeed_9am",
                             "WindSpeed_3pm",
                             "Humidity_9am",
                             "Humidity_3pm",
                             "Pressure_9am",
                             "Pressure_3pm",
                             "Cloud_9am",
                             "Cloud_3pm",
                             "Temp_9am",
                             "Temp_3pm")

names(weatherData)[21] = c("Risk_MM")

         

# Correlation matrix
# Select all the columns with numeric data
numericData = select_if(weatherData,is.numeric)
chart.Correlation(numericData, histogram=TRUE, pch=19)


## ============================= Question 1 ================================== 
##              How to determine whether it will rain tomorrow ?              ##
## ========================================================================== ##

# ---------------------------- Analysis 1 ------------------------------------
# Determine the relationship between Risk_MM and Clouds formation
# ----------------------------------------------------------------------------#

# Create a new column that indicates whether sky is more than half cloudy,
# Cloud more than 4 oktas is more than half cloudy
  # As clouds are measured by (oktas) discrete scale of 0-8, 
  # convert them into factor

weatherData %>%
  mutate(moreHalfCloudy = factor(weatherData$Cloud_3pm,
                                 levels = c(8,7,6,5,4,3,2,1,0),
                                 labels = c(rep("TRUE",4),rep("FALSE",5)))) %>%
  ggplot(aes(factor(Cloud_3pm),Risk_MM)) + 
  geom_jitter(aes(color = moreHalfCloudy)) + 
  labs (title = "Relationship between Risk_MM and Clouds at 3pm",
        subtitle = "Does presence of clouds at 3pm influence the risk of raining tomorrow",
        x = "Clouds at 3pm (oktas)",
        y = "Amount of predicted rainfall tomorrow (mm)",
        color = "Is the sky more than half cloudy ?") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(),axis.title.y = element_text())+
  scale_color_brewer(palette = "Dark2")
 


# ---------------------------- Analysis 2 ------------------------------------
# Determine the relationship between sunshine and Risk_MM
# ----------------------------------------------------------------------------#

# Create a new column that determines whether sunshine hours is above average

weatherData %>%
  filter(!is.na(Sunshine)) %>%       # filter missing sunshine values
  mutate(moreAvgSunshine = factor(Sunshine > mean(weatherData$Sunshine,na.rm = T),
                                  levels = c("TRUE","FALSE"))) %>% 
  ggplot(aes(Sunshine,Risk_MM)) + 
  geom_jitter(aes(color = moreAvgSunshine)) + 
  labs (title = "Relationship between Risk_MM and Sunshine hours",
        subtitle = "Does sunshine influence the risk of raining tomorrow?",
        x = "Sunshine (hours)",
        y = "Amount of predicted rainfall tomorrow (Millimeter)",
        color = "Is the sunshine hours higher than average?") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(),axis.title.y = element_text())+
  scale_color_brewer(palette = "Set1") +
  scale_x_continuous(breaks = seq(0,14,2)) # scale x-axis manually 


# Determine the relationship between sunshine & Clouds

# Create a new column that determines whether sunshine hours is above average
weatherData %>%
  filter(!is.na(Sunshine)) %>%  # filter out missing values
  mutate(moreAvgSunshine = factor(Sunshine > mean(weatherData$Sunshine,na.rm = T),
                                  levels = c("TRUE","FALSE"))) %>% 
  ggplot(aes(Sunshine,Risk_MM)) + 
  geom_jitter(aes(color = moreAvgSunshine,shape=RainTomorrow)) + 
  labs (title = "Relationship between Risk_MM,Sunshine hours,Clouds at 3pm",
        subtitle = "Does clouds influence the number of sunshine hours?",
        x = "Sunshine (hours)",
        y = "Amount of predicted rainfall tomorrow (Millimeter)",
        color = "Is the sunshine hours higher than average?",
        shape = "Will it rain tomorrow ?") + 
  facet_wrap(~Cloud_3pm)+
  theme_igray() +
  scale_color_brewer(palette = "Set1") +
  scale_shape_manual(values = c(16,17)) # scale shape of the point manually


# ---------------------------- Analysis 3 ------------------------------------
# Determine the relationship for pressure at 3pm and rain tomorrow
# ---------------------------------------------------------------------------- #

# Density graph for pressure at 3pm 
weatherData %>%
  ggplot(aes(Pressure_3pm)) + 
  geom_density(aes(fill = RainTomorrow),alpha = 0.6) + 
  labs(title = "Relationship between Pressure at 3pm and Rain Tomorrow",
       subtitle = "Does lower pressure at 3pm cause raining tomorrow?",
       x = "Pressure at 3pm (hPA)",
       y = "Density",
       fill = "Will it rain tomorrow? ") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(),axis.title.y = element_text()) +
  scale_fill_brewer(palette = "Set2")


# Determine what is the maximum and minimum pressure that causes rain tomorrow

# group the data by rain tomorrow
pressureRain = weatherData %>%
  group_by(RainTomorrow) %>%
  summarise(minPressure = min(Pressure_3pm),
            avgPressure = mean(Pressure_3pm),
            maxPressure = max(Pressure_3pm)) %>% View



# ---------------------------- Analysis 4  ------------------------------------
# Determine how pressure influence rain tomorrow with sunshine and clouds at 3pm
# -----------------------------------------------------------------------------#

# Create a new column that determines whether sunshine is above average
weatherData %>%
  filter(!is.na(Sunshine)) %>%
  mutate(moreAvgSunshine = factor(Sunshine > mean(weatherData$Sunshine,na.rm = T),
                                  levels = c("TRUE","FALSE"))) %>%
  ggplot(aes(Sunshine,Pressure_3pm)) + 
  geom_line(aes(color = RainTomorrow),size = 1) + 
  labs(title = "Relationship between sunshine,pressure at 3pm, clouds at 3pm",
       subtitle = paste("Does atmospheric pressure causes rain tomorrow,",
                        "given high sunshine and low clouds formation?"),
       x = "Sunshine (hours)",
       y = "Pressure at 3pm (hPA)",
       color = "Will it rain tomorrow? ") +
  facet_wrap(~Cloud_3pm)+
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(),axis.title.y = element_text()) +
  scale_color_brewer(palette = "Dark2") +
  scale_x_continuous(breaks = c(0,2,4,6,8,10,12,14)) # scaling x-axis manually


# Get the average,minimum, maximum pressure for cloudy skies  
weatherData %>%
  group_by(Cloud_3pm,RainTomorrow) %>%
  summarise(minPressure = min(Pressure_3pm),
            avgPressure = mean(Pressure_3pm),
            maxPressure = max(Pressure_3pm)) %>% View

## ============================= Question 2 ================================= 
# How to determine a severe wind gust that can cause damage ?                 ##
# =========================================================================== ##

# --------------------------- Functions --------------------------------------
# Function name : transformBeaufortScale
# Description   : transform the wind speed into beaufort scale 
# Parameter     : Vector containing wind speeds in KM/H
# Return        : Vector containing the beaufort scale for each wind speed passed

transformBeaufortScale = function (windSpeed) {
  dataReturn = c()
  index = 1
  
  while (index <= length(windSpeed)) {
    if (windSpeed[index] < 1) {
      dataReturn[index] = 0
    } else if (windSpeed[index] <= 5) {
      dataReturn[index]= 1
    } else if (windSpeed[index] <= 11) {
      dataReturn[index] = 2
    } else if (windSpeed[index] <= 19) {
      dataReturn[index] = 3
    } else if (windSpeed[index] <=28) {
      dataReturn[index] = 4
    } else if (windSpeed[index] <= 38) {
      dataReturn[index] = 5
    } else if (windSpeed[index] <= 49) {
      dataReturn[index] = 6
    } else if (windSpeed[index] <= 61) {
      dataReturn[index] = 7
    } else if (windSpeed[index] <= 74) {
      dataReturn[index] = 8
    } else if (windSpeed[index] <= 88) {
      dataReturn[index] = 9
    }else if (windSpeed[index] <= 102) {
      dataReturn[index] = 10
    }else if (windSpeed[index] <= 117) {
      dataReturn[index] = 11
    }else{
      dataReturn[index] = 12
    }
    index = index + 1
  }
  return (dataReturn)
}


# ---------------------------- Analysis 1 -----------------------------------
# Which direction of wind gust has the fastest windGust speed  on average?
# --------------------------------------------------------------------------- #

# Create new column that contains Beaufort scale that describe the wind gust speed
# Create new column that contains wind speed description based on Beaufort scale

data = weatherData %>%
  filter(!is.na(WindGust_Speed) & !is.na(WindGust_Direction)) %>% 
  mutate(BeaufortScale = factor(transformBeaufortScale(WindGust_Speed)),
         WindDescription  = factor(BeaufortScale,
                                   levels = 0:12,
                                   labels = c(rep("Calm - Gentle Breeze",4),
                                              rep("Moderate Breeze",2),
                                              rep("Strong Breeze - Strong Gale",4),
                                              rep("Storm - Hurricane",3))))

# Plot the wind speed force for each direction
data %>%
  ggplot(aes(BeaufortScale)) +
  geom_bar(aes(fill = WindDescription)) +
  labs(title = "Relationship between wind gust speed and direction",
       subtitle = "Which wind gust direction is the most devastating ?",
       x = "How destructive is the wind gust ? (Beaufort Scale)",
       y = "Frequency",
       fill = "Description of wind force") +
  facet_wrap(~WindGust_Direction) +
  theme_stata() +
  theme(axis.title.x = element_text(),axis.title.y = element_text()) +
  scale_y_continuous(breaks = c(0,5,10,15,20,25)) +
  scale_fill_brewer(palette = "Reds")




 # ----------------------------- Analysis 2 ----------------------------------
# Determine the relationship between atmospheric pressure and wind gust
# --------------------------------------------------------------------------- #

# Create new column that contains Beaufort scale that describe the wind gust speed
# Create new column that contains wind speed description based on Beaufort scale

# Use pressure at 9am
data = weatherData %>%
  filter (!is.na(WindGust_Speed)) %>% # filter out missing wind gust values
  mutate(BeaufortScale = transformBeaufortScale(WindGust_Speed),
         WindDescription  = factor(BeaufortScale,
                                   levels = 0:12,
                                   labels = c(rep("Calm - Gentle Breeze",4),
                                              rep("Moderate Breeze",2),
                                              rep("Strong Breeze - Strong Gale",4),
                                              rep("Storm - Hurricane",3))))
data %>%
  ggplot(aes(Pressure_9am,WindGust_Speed)) +
  geom_point(aes(color = WindDescription)) +
  geom_smooth(method = "lm") +  # Using liner model for smooth function
  labs(title = "Relationship between wind gust speed and pressure at 9am",
       subtitle = "Will atmospheric pressure influence wind gust speed?",
       x = "Pressure at 9am (hPA)",
       y = "Wind Gust Speed (KM/H)",
       color = "Description of wind force") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(),axis.title.y = element_text()) +
  scale_color_brewer(palette = "Set1")
  

# Use pressure at 3pm
data = weatherData %>%
  filter (!is.na(WindGust_Speed)) %>%   # filter out missing wind gust values
  mutate(BeaufortScale = transformBeaufortScale(WindGust_Speed),
         WindDescription  = factor(BeaufortScale,
                                   levels = 0:12,
                                   labels = c(rep("Calm - Gentle Breeze",4),
                                              rep("Moderate Breeze",2),
                                              rep("Strong Breeze - Strong Gale",4),
                                              rep("Storm - Hurricane",3))))

data %>%
  ggplot(aes(Pressure_3pm,WindGust_Speed)) +
  geom_jitter(aes(color = WindDescription)) +
  geom_smooth(method = "lm") +  # Using liner model for smooth function
  labs(title = "Relationship between pressure at 3pm and wind gust speed",
       subtitle = "Will atmospheric pressure influence wind gust speed?",
       x = "Pressure at 3pm (hPA)",
       y = "Wind Gust Speed (KM/H)",
       color = "Description of wind force") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(),axis.title.y = element_text()) +
  scale_color_brewer(palette = "Set1")


# ----------------------------- Analysis 3 ----------------------------------
# Determine the relationship between wind speed and wind gust speed 
# --------------------------------------------------------------------------- #

# Create new column that contains Beaufort scale that describe the wind gust speed
# Create new column that contains wind speed description based on Beaufort scale
# Create new column that contains average wind speed of the day

data = weatherData %>%
  filter(!is.na(WindGust_Speed),!is.na(WindSpeed_9am)) %>%
  mutate(BeaufortScale = transformBeaufortScale(WindGust_Speed),
         avgWindSpeed = (WindSpeed_9am + WindSpeed_3pm)/2 ,
         WindDescription  = factor(BeaufortScale,
                                   levels = 0:12,
                                   labels = c(rep("Calm - Gentle Breeze",4),
                                              rep("Moderate Breeze",2),
                                              rep("Strong Breeze - Strong Gale",4),
                                              rep("Storm - Hurricane",3))))

data %>%
  ggplot(aes(avgWindSpeed,WindGust_Speed)) +
  geom_point(aes(color = WindDescription)) +
  geom_smooth(method = "lm") +
  labs(title = "Relationship between wind speed and wind gust speed",
       subtitle = "How wind speed influence wind gust speed?",
       x = "Average wind speed (KM/H)",
       y = "Wind gust speed (KM/H)",
       color = "Description of wind gust force") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(), axis.title.y = element_text()) +
  scale_color_brewer(palette = "Set1")




# ============================= Question 3 ===================================
# How to find the days that are snowing and its condition                     ##
#  ========================================================================== ##

# How does snow form : moisture , low temperature 

# ---------------------------- Analysis 1 ----------------------------------- 
# Determine the number of days that are cold enough to snow 
# ---------------------------------------------------------------------------- #

# Snowing can happens as low as 2 Celsius
# Create a new column that determines whether minimum 
  # temperature is below 2 Celsius
# Group by the days that below 2 Celsius and above 2 Celsius



weatherData %>%
  mutate (SnowToday = factor(MinTemp<2,levels = c("TRUE","FALSE"))) %>%
  group_by(SnowToday) %>%
  summarise(count = n()) %>%
  ggplot(aes(SnowToday,count)) +
  geom_bar(aes(fill = SnowToday),stat = "identity",show.legend = FALSE) +
  geom_text(aes(label = count),vjust = -0.2) +
  labs(title = "Distribution of days",
       subtitle = "How many days were snowing ?",
       x = "Did it snow today? (Below 2 degree Celsius) ",
       y = "Frequency") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(), axis.title.y = element_text()) +
  scale_fill_brewer(palette = "Set2")

# ---------------------------- Analysis 2 ----------------------------------- 
# Determine the temperature changes for snowy days
# --------------------------------------------------------------------------- #

# Determine whether 9am is the coldest temperature


# Create a new column that determines whether temperature has increased

weatherData %>%
  filter(MinTemp < 2) %>%
  mutate(tempIncrease = factor(Temp_3pm > Temp_9am)) %>%
  ggplot(aes(Temp_9am,Temp_3pm,color = tempIncrease)) +
  geom_point() +
  geom_smooth() +
  geom_hline(aes(yintercept = 2,
                 linetype = "2 degree Celsius (Snowing Temperature)"),
             color = "skyblue4", size = 1)+
  geom_vline(aes(xintercept = 2),
             color = "skyblue4", size = 1) +
  labs (title = "Temperature changes for snowy days",
        subtitle = "Temperature changes from 9am to 3pm",
        x = "Temperature at 9am (Celsius)",
        y = "Temperature at 3pm (Celsius)",
        linetype = "",
        color = "Temperature increased from 9am to 3pm") +
  theme_igray()


# ---------------------------- Analysis 3 ------------------------------------ 
# Determine the humidity level for snowy days
# ----------------------------------------------------------------------------#

# Filter out the days that has higher minimum temperature 
# Use 9am scale since 9am is cooler than 3pm, more likely to snow
# Snow9am is a new column that determines whether temperature at 9am is 
  # below 2 degree Celsius

weatherData %>%
  filter(MinTemp < 2) %>%
  mutate(Snow9am = factor(Temp_9am < 2)) %>%
  ggplot(aes(Temp_9am, Humidity_9am)) +
  geom_point(aes(color = Snow9am)) + 
  geom_smooth(method = "lm",alpha = .5)+
  labs(title = "Study the moisture for snowy days",
       subtitle = "Relative Humidity at 9am for snowy days ",
       x = "Temperature at 9am (Celsius)",
       y = "Relative Humidity at 9am (%)",
       color = "Snowing at 9am") +
  theme_igray()


# ---------------------------- Analysis 4 ----------------------------------- 
# Determine whether it will have blizzard at 9am
# --------------------------------------------------------------------------- #

# Plot it to determine wind speed for days that are still snowing at 9am
# Blizzard need a sustained wind speed of 56 KM/H

weatherData %>%
  filter(Temp_9am < 2,!is.na(WindSpeed_9am)) %>% # filter out missing values
  ggplot(aes(Temp_9am,WindSpeed_9am)) +
  geom_point() +
  geom_hline(aes(yintercept = 56,
                 linetype = "Minimum sustained wind speed to achieve blizzard"),
             color = "Red",size = 1) +
  labs(title = "Study wind speed at 9am for snowy days",
       subtitle = "Are there a blizzard recorded at 9am ?",
       x = "Temperature at 9am (Celsius)",
       y = "Wind Speed at 9am (KM/H)",
       linetype = "") +
  theme_igray() +
  scale_y_continuous(breaks = seq(0,60,10)) +
  scale_linetype_manual(values = c(4)) # changing the line-type 



## ============================= Question 4 ================================= 
##              How to find the days that are likely to spark a wildfire ?    ##
## ========================================================================== ##

# ----------------------------- Extra Exploration ---------------------------#
# weather can spark wildfire in certain conditions 
# Source : https://www.noaa.gov/stories/ask-scientist-how-can-weather-spark-and-spread-wildfires 
# Three main ingredients : Temperature & Moisture & Wind Speed

# ----------------------------- Functions -------------------------------------
# Function    : transformHumidity
# Description : transform humidity scale to humidity level
# Parameter   : Vector containing the humidity scale
# Return      : Vector containing the description of humidity level

# Reference : https://www.aprilaire.com/benefits/preservation/relative-humidity-chart


transformHumidity = function (humidity) {
  transformedScale = c()
  index = 1
  while (index <= length(humidity)) {
    if (humidity[index] < 30) {
      transformedScale[index] = "Too dry"
    } else if (humidity[index] <= 60) {
      transformedScale[index] = "Healthy"
    } else {
      transformedScale[index] = "Too moist"
    }
    index = index + 1
  }
  return (transformedScale)
}


# ---------------------------- Analysis 1 -----------------------------------
# Determine the hottest time of the day
# ---------------------------------------------------------------------------- #

# Find whether temperature increase from 9am to 3pm for all the days
# Create a new column that determines whether temperature at 3pm is bigger than 9am

weatherData %>%
  mutate(tempIncrease = factor(Temp_3pm > Temp_9am)) %>%
  ggplot(aes(Temp_9am,Temp_3pm,color = tempIncrease)) +
  geom_point() +
  labs (title = "Temperature changes for all the days",
        subtitle = "Temperature changes from 9am to 3pm",
        x = "Temperature at 9am (Celsius)",
        y = "Temperature at 3pm (Celsius)",
        linetype = "",
        color = "Temperature increased from 9am to 3pm") +
  theme_igray() +
  scale_y_continuous(breaks = seq(0,40,5))



#  ---------------------------- Analysis 2 -----------------------------------
# Determine the relationship between humidity and temperature
# ---------------------------------------------------------------------------- #

# Use transformHumidity function to determine the humidity scale relative to human 
  # comfort with three categories : "Too dry" , "Healthy", "Too moist"

# Temperature 3pm vs Humidity 3pm

weatherData %>%
  mutate(HumidityScale = factor(transformHumidity(Humidity_3pm),
                                levels = c("Too dry","Healthy","Too moist"))) %>%
  ggplot(aes(Temp_3pm,Humidity_3pm)) +
  geom_jitter(aes(color = HumidityScale)) +
  geom_smooth(method = "lm") +   
  labs(title = "Relationship between temperature and humidity",
       subtitle = "Will temperature influence the relative humidity? ",
       x = "Temperature at 3pm (Celsius)",
       y = "Relative humidity at 3pm (%)",
       color = "Humidity scale") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(), axis.title.y = element_text()) +
  scale_x_continuous(breaks = seq(0,35,5)) +   
  scale_y_continuous(breaks = seq(0,100,10)) + 
  scale_color_brewer(palette = "Set1")


#  ---------------------------- Analysis 3 -----------------------------------
# Determine the relationship between sunshine and temperature
# ----------------------------------------------------------------------------#

# Use transformHumidity function to determine the humidity scale relative to human 
  # comfort with three categories : "Too dry" , "Healthy", "Too moist"

weatherData %>%
  filter(!is.na(Sunshine)) %>%  # filter out missing sunshine values
  mutate(HumidityScale = factor(transformHumidity(Humidity_3pm),
                                levels = c("Too dry",
                                           "Healthy",
                                           "Too moist"))) %>%
  ggplot(aes(Sunshine,MaxTemp)) +
  geom_jitter(aes(color = HumidityScale)) +
  geom_smooth() +
  labs(title = "Relationship between sunshine and temperature",
       subtitle = "Will sunshine influence temperature ? ",
       x = "Sunshine (hours)",
       y = "Maximum temperature (Celsius)",
       color = "Humidity scale") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(), axis.title.y = element_text()) + 
  scale_color_brewer(palette = "Set1")

#  ---------------------------- Analysis 4 -----------------------------------
# Determine the relationship between evaporation and temperature
# ----------------------------------------------------------------------------#

# Get the value for mean and 3rd Quartile for evaporation
summary(weatherData$Evaporation)
# Mean = 4.522
# Third Quartile = 6.400

# Get the value for 3rd Quartile for maximum temperature as reference
summary(weatherData$MaxTemp)
# Third Quartile = 25.5

# Use transformHumidity function to determine the humidity scale relative to human 
  # comfort with three categories : "Too dry" , "Healthy", "Too moist"

data = weatherData %>%
  filter(MaxTemp > 25.5) %>%
  mutate(HumidityScale = factor(transformHumidity(Humidity_3pm),
                                levels = c("Too dry",
                                           "Healthy",
                                           "Too moist")))

data %>%
  ggplot(aes(MaxTemp,Evaporation)) +
  geom_point(aes(color = HumidityScale)) +
  geom_hline(aes(linetype = "3rd Quartile", yintercept = 6.4),
             size = 1,color = "tan2") +
  geom_hline(aes(yintercept = 4.5,linetype = "Mean"),
             size = 1,color = "tan2") +
  labs(title = "Relationship between temperature and evaporation",
       subtitle = "Evaporation on hot days (>25.5 degree Celsius)",
       x = "Maximum temperature (Celsius)",
       y = "Evaporation (mm)",
       color = "Humidity scale",
       linetype = "") +
  theme_igray() +
  scale_color_brewer(palette = "Set1")


#  ---------------------------- Analysis 5 -----------------------------------
# Find the average wind speed on a hot day & without rain
# ----------------------------------------------------------------------------#

# get third quartile of temperature 3pm
summary(weatherData$Temp_3pm)
# 3rd quartile of Temp 3pm = 24


# only select days that has Temp_3pm above 3rd quartile and did not rain 

data = weatherData %>%
  filter(Temp_3pm > 24, RainToday == "No") %>%
  mutate(HumidityScale = factor(transformHumidity(Humidity_3pm),
                                levels = c("Too dry",
                                           "Healthy",
                                           "Too moist"))) 


# Danger condition is when dry and wind speed above 32 km/h

data %>%
  ggplot(aes(Temp_3pm,WindSpeed_3pm)) +
  geom_jitter(aes(color = HumidityScale)) +
  geom_hline(aes(yintercept = 32,linetype = "Wind speed that spreads wildfire easily"),
             color = "red3",size = 1) +
  labs(title = "Wind speed on hot days ",
       subtitle = "Determine the wind speed on hot days (more than 24 Celsius)",
       x = "Temperature at 3pm (Celsius)",
       y = "Wind speed at 3pm (KM/H)",
       color = "Humidity scale",
       linetype = "") +
  theme_igray()

# ---------------------------- Analysis 6 -----------------------------------
# Estimate the days that are prone to spark a wildfire easily
# ---------------------------------------------------------------------------- #
# Four factors : Sunshine, Humidity, Wind gust force,Evaporation (discover above)

# Function name : determineWildfire 
# Description   : Determine the likelihood of sparking a wildfire given the weather condition

# Parameter     : Sunshine (Vector containing the sunshine for each day) ,

#                 Humidity (Vector containing the relative humidity at 3pm), 

#                 WindSpeed
#                 (Vector containing the wind speed at 3pm), 

#                 Evaporation (Vector containing the evaporation for each day)

# Return        : Vector containing the percentage of sparking of wildfire for 
#                 each day based on four condition

determineWildfire = function(Sunshine,Humidity,Windspeed,Evaporation) {
  WildfireChances = c()
  index = 1
  while (index <= length(Sunshine)) {
    percentage = 0
    if (Sunshine[index] >= 10) {    # sunshine above/near third quartile
      percentage = percentage + 25
    } 
    if (Humidity[index] < 30) {     # Relative humidity less than 30% == too dry
      percentage = percentage + 25
    }
    if (Windspeed[index] >= 32) {   # wind speed >= 32 likely to cause wildfire
      percentage = percentage + 25
    }
    if (Evaporation[index] > 6.4) { # Evaporation more than 3rd quartile
      percentage = percentage + 25
    }
    WildfireChances[index] = percentage
    index = index + 1
  }
  return (WildfireChances)
}

# Wildfire prediction is done on days that are : 
  # above 25.5 Celsius maximum temperature,
  # did not rain

# LikeliHood_Wildfire contains the values returned by determineWildfire model

# Those values are categorize into five categories : 
#  "Very likely"   (4 values met the condition)  
#  "Likely"        (3 out of 4 values met the conditions)
#  "Neutral"       (2 out of 4 values met the conditions)
#  "Unlikely"      (1 out of 4 values met the conditions)
#  "Very Unlikely" (0 put of 4 values met the conditions)

# 4 values are sunshine, humidity 3pm, wind speed 3pm, and evaporation

data = weatherData %>%
  filter(!is.na(Sunshine),RainToday == "No",MaxTemp > 25.5) %>%
  mutate(Likelihood_Wildfire = factor(determineWildfire(Sunshine,
                                                        Humidity_3pm,
                                                        WindSpeed_3pm,
                                                        Evaporation),
                                      levels = c(100,75,50,25,0),
                                      labels = c("Very Likely", 
                                                 "Likely",
                                                 "Neutral",
                                                 "Unlikely", 
                                                 "Very Unlikely")) )

data %>%
  ggplot(aes(Temp_3pm,MaxTemp)) +
  geom_jitter(aes(color = Likelihood_Wildfire)) + 
  labs (title = "Rough estimate of the likelihood of wildfire",
        subtitle = "Determine the chances of wildfire, for hot days (> 25 degree Celsius)",
        x = "Temperature at 3pm (Celsius)",
        y = "Maximum temperature (Celsius)",
        color = "Chances of sparking a wildfire") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(),axis.title.y = element_text()) +
  scale_color_brewer(palette = "Set1")

# Interactive Plot
ggplotly()

# Plot the distribution 
data %>%
  group_by(Likelihood_Wildfire) %>%
  summarise(count = n()) %>% 
  ggplot(aes(Likelihood_Wildfire,count)) +
  geom_bar(aes(fill = Likelihood_Wildfire),stat = "identity",show.legend = F) +
  geom_text(aes(label = count),vjust = -0.2) +
  labs(title = "Distribution of likelihood to spark wildfire",
       subtitle = "How many days are likely to spark wildfire?",
       x = "Chances of sparking a wildfire",
       y = "Frequency") +
  theme_fivethirtyeight() +
  theme(axis.title.x = element_text(),axis.title.y = element_text()) +
  scale_fill_brewer(palette = "RdYlGn")

## ============================= Question 5 ================================== 
##                Find the days that has thunderstorm  ?                      ##
## ===========================================================================##

# ------------------------------ Function -----------------------------------
# determine the 1st and 3rd quartile of rainfall to categorize the rain
weatherData %>%
  filter(RainToday == "Yes") %>%
  select(Rainfall) %>%
  summary()

# Function Name : transformRainfallScale 
# Description   : Categorize the rainfall amount into different categories
# Parameter     : Vector containing rainfall amount for each day
# Return        : Vector that describes the category of rainfall amount for each day

transformRainfallScale = function(rainfall){
  transformed = c()
  index = 1
  while(index <= length(rainfall)){
    if (rainfall[index]<3){              # Below first quartile
      transformed[index] = "Light rain"
    } else if (rainfall[index]<10.25){   # Below third quartile
      transformed[index] = "Moderate rain"
    } else {
      transformed[index] = "Heavy rain"
    }
    index = index + 1
  }
  return (transformed)
}


#  ---------------------------- Analysis 1 -----------------------------------
# Determine the relationship between rainfall and pressure
# ---------------------------------------------------------------------------- #
weatherData %>%
  filter(RainToday == "Yes") %>% 
  mutate(rainCategory = factor(transformRainfallScale(Rainfall),
                               levels = c("Light rain",
                                          "Moderate rain",
                                          "Heavy rain")),
         avgPressure = (Pressure_9am+Pressure_3pm)/2) %>%
  ggplot(aes(rainCategory,avgPressure,fill = rainCategory)) +
  geom_boxplot(outlier.alpha = 0,show.legend = F) +
  geom_point(show.legend = F) +
  labs(title = "Relationship between rainfall amount and pressure",
       subtitle = "Study the atmospheric pressure for each rainfall category",
       x = "Rainfall Category",
       y = "Average pressure (hPA)") +
  theme_minimal()

# Interactive plot
ggplotly()

#  ---------------------------- Analysis 2 -----------------------------------
# Determine the clouds formation at 9am and 3pm and rainfall for 
# the day
# ----------------------------------------------------------------------------#

# Only select the days that rain &
  # Group values by clouds at 9am & clouds at 3pm &
  # Summarize the values by finding average rainfall

# Create a new column that contains the determines the highest clouds formation
  # between 9am and 3pm (higher clouds means rain)

# Categorize rainfall into three different categories 

data = weatherData %>%
  filter(RainToday == "Yes") %>%
  group_by(Cloud_9am,Cloud_3pm) %>% 
  summarise(count = n(),
            averageRainfall = mean(Rainfall)) %>% 
  mutate(maxCloud = max(Cloud_9am,Cloud_3pm),
         rainfallCat = factor(transformRainfallScale(averageRainfall),
                              levels = c("Light rain",
                                         "Moderate rain",
                                         "Heavy rain")))

data %>% 
  ggplot() +
  geom_point(aes(maxCloud,averageRainfall,size = count,color = rainfallCat)) +
  labs(title = "Relationship between rainfall and cloud formation",
       subtitle = "Average rainfall for rainy days categorized by morning clouds and evening clouds",
       x = "Clouds (oktas)",
       y = "Rainfall (MM)",
       size = "Frequency",
       color = "Rainfall category") +
  theme_minimal()

# Interactive plot  
ggplotly()






