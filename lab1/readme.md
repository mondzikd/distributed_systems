## Zadanie 1

Zaimplementować dwukierunkową komunikację przez UDP Java-Java
– Klient wysyła wiadomość i odczytuje odpowiedź
– Serwer otrzymuje wiadomość i wysyła odpowiedź
– Należy pobrać adres nadawcy z otrzymanego datagramu


## Zadanie 2

Zaimplementować komunikację przez UDP pomiędzy językami Java i Python
– JavaUdpServer + PythonUdpClient
– Należy przesłać wiadomość tekstową:
> żółta gęś
*(uwaga na kodowanie)*


## Zadanie 3

Zaimplementować przesył wartości liczbowej w przypadku JavaUdpServer + PythonUdpClient
– Symulujemy komunikację z platformą o innej kolejności bajtów: klient Python ma wysłać następujący ciąg bajtów:
'''
msg_bytes = (300).to_bytes(4, byteorder='little')
'''
– Server Javy ma wypisać otrzymaną liczbę oraz odesłać liczbę zwiększoną o jeden


## Zadanie 4

Zaimplementować serwer (Java lub Python) który rozpoznaje czy otrzymał wiadomość od klienta Java czy od klienta Python i wysyła im różne odpowiedzi (np.
‘Pong Java’, ‘Pong Python’)


## Zadanie 5

Napisać aplikację typu chat
– Klienci łączą się serwerem przez protokół TCP
– Serwer przyjmuje wiadomości od każdego klienta i rozsyła je do pozostałych (wraz z id/nickiem klienta)
– Serwer jest wielowątkowy – każde połączenie od klienta powinno mieć swój wątek
– Proszę zwrócić uwagę na poprawną obsługę wątków

Dodać dodatkowy kanał UDP
– Serwer oraz każdy klient otwierają dodatkowy kanał UDP (ten sam numer portu jak przy TCP)
– Po wpisaniu komendy ‘U’ u klienta przesyłana jest wiadomość przez UDP na serwer, który rozsyła ją do pozostałych klientów
– Wiadomość symuluje dane multimedialne (można np. wysłać ASCII Art)

Zaimplementować powyższy punkt w wersji multicast
– Nie zamiast, tylko jako alternatywna opcja do wyboru (komenda ‘M’)
– Multicast przesyła bezpośrednio do wszystkich przez adres grupowy (serwer może, ale nie musi odbierać)

Uwagi:
- Zadanie można oddać w dowolnym języku programowania
- Nie wolno korzystać z frameworków do komunikacji sieciowej – tylko gniazda! Nie wolno też korzystać z Akka
