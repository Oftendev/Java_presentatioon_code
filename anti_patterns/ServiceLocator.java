package anti_patterns;
import java.util.Map;
import java.util.HashMap;


public class ServiceLocator {
    private static Map<Class<?>, Object> services = new HashMap<>();
    /* Map - интерфейс, который реализуется в Hashmap и других
    Т.е. можно писать hashmap вместо map, но это дурной тон, т.к. при изменении Hashmap
    на что-то другое придётся менять обе части


    <?> - wildcard - подстановочный символ (в джененриках), обозначает неизв. тип
    <T> - параметр типа (переменная, в которую подставляется конкретный тип)
    Основные виды Wildcards
    - Неограниченный (<?>): Соответствует любому типу. Его используют, когда логика метода не зависит от конкретного типа данных (например, list.size() или list.clear()). Из такой коллекции можно только читать объекты типа Object, а добавлять в неё ничего нельзя (кроме null).
    - Ограниченный сверху (? extends T): Соответствует типу T или любому его наследнику. Это устанавливает "верхнюю границу". Используется для чтения данных (Producer), так как компилятор гарантирует, что каждый элемент является как минимум типом T.
    - Ограниченный снизу (? super T): Соответствует типу T или любому его родителю. Это устанавливает "нижнюю границу". Используется для записи данных (Consumer), так как в такую коллекцию гарантированно можно положить объект типа T. 
    Принцип PECS (Producer Extends, Consumer Super) 
    Этот мнемонический принцип помогает выбрать правильный wildcard: 
    Producer (Производитель) → extends: Если вы только читаете данные из структуры, используйте <? extends T>.
    Consumer (Потребитель) → super: Если вы только записываете данные в структуру, используйте <? super T>. 
    */
    
    //здесь <T> - объявление дженерика(шаблона), т.е. мы говорим компилятору, что в этом методе тип T будет зависеть от того, что передаст пользователь
    public static <T> void register(Class<T> type, T instance){
        services.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> type) {
        return (T)services.get(type);
    }

    interface Logger {
        void log(String msg);
    }
    
    static class ConsoleLogger implements Logger {
        @Override
        public void log(String msg) { System.out.println("[LOG] " + msg); }
    }
    //Здесь мы испоьзуем ServiceLocator
    static class UserService {
        public void process() {
            Logger logger = ServiceLocator.get(Logger.class);
            logger.log("Processing user");
        }
    }

    public static void demo(){
        System.out.println("\n----ServiceLocator antipattern----");
        ServiceLocator.register(Logger.class, new ConsoleLogger());
        UserService service = new UserService();
        service.process();
    }
    /* 
    Минумы - получается, что ServiceLocator своего рода чёрный ящик. Т.е. снаружи метод process выглядит самодостаточным, а внутри он оказывается требует ещё и логгер + создаётся жёсткая зависимость класса от глобального локатора + при тестировании придётся собрать глоабльное состоянии локатора
    */
}
