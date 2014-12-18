OTG_daemon
=========
Demon для устройства на ARM'е. При подсоединении к хосту демон висит на USB OTG и выполняет простые функции для настройки wireless, т.е. сканирует сети, сохраняет настройки в /etc/network/interface, поднимает интерфейс.
Сейчас USB Gadget грузит драйвер g_serial, поэтому функционирует com port, но при дальнейшей разработке от этого надо будет уйти. Логика, за счет интерфесов (IObserver и IObservable), должна остаться. При добавлении нового драйвера OTG, необходимо создать прослойку в USB_OTG, наследовать IObserver  и реализовать notifyObserver.
Для доступа к serial из java используется jSSC - https://code.google.com/p/java-simple-serial-connector/
Запуск скриптов через udev /etc/udev/rules.d/50-udev-local.rules


