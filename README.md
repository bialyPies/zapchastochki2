# zapchastochki2

Данное приложение "Магазин запчастей" было разработано в рамках курсового проекта "Программное обеспечение информационной безопасности мобильных систем".

Мобильное приложение имеет простой, интуитивно понятный пользователю интерфейс и выполняет следующие функции:
- возможность регистрации;
- представление каталога;
- возможность добавления детали в корзину;
- возможность создания заказа;
- добавление, редактирование и удаление детали;
- поиск заказа по клиенту;
- поиск детали по названию;
- просмотр заказов;
- изменение статуса заказов;
- изменение статуса детали в заказе;
- возможность просмотра профиля, изменение данных;

В целях безопасности были реализованы следующие пункты:

1 Шифрование базы данных с помощью SQLCipher. Для этого были добавлены следующие зависимости в build.gradle apk:
```yaml
  implementation "androidx.sqlite:sqlite:2.0.1"
  implementation 'net.zetetic:android-database-sqlcipher:4.3.0@aar'
```
Для работы с классами добавили импорты 
```
  import net.sqlcipher.database.SQLiteDatabase;
  import net.sqlcipher.database.SQLiteOpenHelper;
```
В классе DBHelper необходимо загрузить необходимые библиотеки и файлы с помощью метода 
```java
  SQLiteDatabase.loadLibs(context)
```
Принцип работы с базой данных не изменился, только теперь для получения базы данных необходимо передавать пароль в параметрах методов 
```
  getReadableDatabase(dbpassword);
  getWritableDatabase(dbpassword);
```

2 Приложение было дополнительно защищено биометерической аутентификацией с помощью fingerprint api. Запрос на биометрическую аутентификацию поступает после входа и каждый раз при открытии приложения.

Работа с биометрической аутентификацией реализована в фрагменте FingerprintFragment

Для проверки возможности использования сенсора и наличия сохраненных отпечатков был использован класс BiometricManager
```
        final BiometricManager biometricManager = BiometricManager.from(ctx);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                /*...*/
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                /*...*/
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                /*...*/
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                /*...*/
                break;
        }
```
Для создания управления стандартным диалогам для биометрической аутентификации использовался класс BiometricPrompt.
```
        Executor executor = ContextCompat.getMainExecutor(ctx);
        final BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) ctx, executor, new BiometricPrompt.AuthenticationCallback(){
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                /*...*/
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Use fingerprint to login")
                .setNegativeButtonText("cancel")
                .build();
```
Для открытия диалога:
```
  biometricPrompt.authenticate(promptInfo);
```
3 Защита приложения от создания скриншотов и записи видео
```
  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
```
4 Обфускация для проекта с помощью ProGuard. В build.gradle minifyEnabled установлено в true. Добавлены правила в proguard-rules.pro.

# Руководство пользователя

Для того чтобы установить приложения на телефон необходимо иметь версию API не ниже 28. Версию Android и API вашего телефона можно узнать в настройках телефона.
Если версия Android подходит, тогда можно устанавливать приложение на мобильный телефон. В меню ваших приложений или на главном экране смартфона появится новая иконка с именем «zapchastochki». Запустим приложение нажатием на иконку приложения. Откроется страница входа или регистрации. Для того, чтобы зайти в приложении необходимо зарегистрироваться и/или войти в уже созданный профиль. 

![alt text](https://sun9-60.userapi.com/impf/d8MF3KLlkPFVBoU0S09o_4a7yNp93208xctUqQ/5IvZwKhLVFA.jpg?size=203x395&quality=96&proxy=1&sign=6c25d47f957b4a6d82d04d34b934b50b&type=album)

Рисунок 5.1 – Активность входа и регистрации

При нажатии кнопки Log In, а также при открытии свернутого приложения в дальнейшем, появится окно с запросом отпечатка пальца (рис. 5.2). После нажатия на кнопку Login откроется окно, отображающее данные процесса аутентификации.
 
![alt text](https://sun9-64.userapi.com/impf/G1nHMoHh_2yhUlkpIl9c83JHhe3a_axl4NfdEw/8fwXq_H5hPo.jpg?size=495x631&quality=96&proxy=1&sign=849a142d21ac6b95f8b2231a5390e477&type=album)

Рисунок 5.2 – Фрагмент аутентификации по отпечатку пальца

После того, как вы войдете или зарегистрируетесь как обычный пользователь и пройдете аутентификацию по отпечатку пальца, вам откроется страница с каталогом всех деталей (рис. 5.3). Стрелка №1 указывает на кнопку поиска по названию детали. Стрелка №2 указывает на элемент каталога. Если нажать на этот элемент, то деталь добавится в корзину. Стрелка №3 указывает на панель навигации, с помощью которой можно попасть в остальные разделы.

![alt text](https://sun9-70.userapi.com/impf/fHOwVP84BOBVqt-N2795zSlJKuthgb0GWZg0vg/Fzo4fY8Lok0.jpg?size=238x468&quality=96&proxy=1&sign=c7fe9986851e72af4c413529068fdb6b&type=album)

Рисунок 5.3 – Страница каталога

После нажатия на элемент каталога он добавляется в корзину, перейти к которой можно выбрав второй элемент слева на панели навигации, называемый «Basket». Перейдя в корзину, вы увидите экран, показанный на рисунке 5.4.

![alt text](https://sun9-33.userapi.com/impf/e9n36eP-D40-rDFkN7k9QRDxx1tGXSJsgjvRTQ/BaCD06_qieE.jpg?size=283x557&quality=96&proxy=1&sign=4a7c4f3500db88574a9950e13788b26d&type=album)

Рисунок 5.4 – Основные элементы управления со страницы корзины

Элементы в корзине также кликабельны. При нажатии на элемент в корзине вам будет предложено добавить деталь в заказ (стрелка №1) или удалить из корзины (стрелка №2). Если вы выбираете добавить деталь в заказ, то сам элемент подсветится желтым цветом. После того, как вы сформировали заказ, нажмите кнопку, указанную стрелкой №3. Вам откроется список деталей, входящих в заказ. Здесь вы можете удалить лишние детали из заказа, кликнув на деталь и выбрав соответствующий пункт меню. Если заказ соответствует вашим желаниям, нажмите снова кнопку «Order», чтобы подтвердить заказ. Если все прошло успешно, высветится окошко, сообщающее, что заказ подтвержден.
Просмотреть свои заказы вы можете нажав на кнопку, отмеченную стрелкой №1 (см. рис. 5.5).

![alt text](https://sun9-30.userapi.com/impf/bkDaZKZXUy3YD74efl09gR-H0mf19eO2GcWDeQ/qPyHhS7g8LA.jpg?size=235x456&quality=96&proxy=1&sign=d2e9c94eba4c4dbc1d92371cae875537&type=album)

Рисунок 5.5 – Основные элементы управления для страницы заказов

Статус заказа указан стрелкой №2. При нажатии на заказ вам откроются его подробности.
Последняя вкладка, доступная пользователю – вкладка профиля (рис. 5.6). На вкладке профиля указана информация пользователя. Здесь можно и нужно изменить поля name и number. Для того, чтобы их изменить, достаточно ввести новый текст и нажать кнопку «Same changes». 

![alt text](https://sun9-10.userapi.com/impf/YyLCLGybHZfqHf2nWdyZqMjVQoMvK3SRU8JAVQ/Ork57S4vvco.jpg?size=235x455&quality=96&proxy=1&sign=8b80f3517b302424569661d7ce312775&type=album)

Рисунок 5.6 – Элементы управления страницы профиля

Если вы вошли в приложении под ролью администратора, вам будет доступно две вкладки в нижнем навигационном меню (рис. 5.7). Стрелка №1 указывает на кнопку, открывающую фрагмент для добавления новой детали. 

![alt text](https://sun9-16.userapi.com/impf/Rh6qwH-8XSHfvPyWYonuZBuI8D8BmZxSvqtSrA/udn_XDTN1jI.jpg?size=168x325&quality=96&proxy=1&sign=7276f1dcd6af462c3b831e8ce40610ed&type=album)

Рисунок 5.7 – Страница администратора

При нажатии на существующую деталь, указанную стрелкой №2, в каталоге будет предложен выбор: удалить или изменить деталь.
На второй вкладке администратору доступны все заказы. При нажатии на заказ, администратору будет предложено изменить статус заказа либо просмотреть его детали, у которых так же можно изменить статус.

