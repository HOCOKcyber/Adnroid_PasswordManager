PasswordManager - приложение для локального хранения паролей. (Без выхода в сеть из-за отсутсвия внешнего сервера)

Что использовал:
- Kotlin
- Jetpack compose
- Room
- Coil

Что может приложение: 
- Совершать действия CRUD с аккаунтом, добавленным пользователем. Аккаунт хранит в себе логин, пароль, домен сайта (используется для показа картинки), название аккаунта/сайта (для удобства поиска)
- На главной странице (Home) отображаются все аккаунты (сначала избранные)
- На странице поиска (Search) можно найти аккаунт по логину или названию аккаунта/сервиса
- Для удаления или добавления в избранное нужно перетащить карточку аккаунта вправо (сделать swipe или slide)
- Для добавления аккаунта можно нажать на FBA (Плюсик на экранах)
- Для просмотра нужно нажать на карточку аккаунта
- Для изменения сначала нажать на карточку аккаунта, зачем нажать изменить
- Кроме того можно легко копировать пароль или поделиться аккаунтом
