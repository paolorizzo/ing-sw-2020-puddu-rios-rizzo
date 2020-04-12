package it.polimi.ingsw;

import org.junit.Test;

public class ObservableTest
{
    /*
    public class ObserverUtil implements Observer
    {
        private boolean flag = false;

        @Override
        public void update(Object o)
        {
            this.flag = true;
        }

        public boolean getFlag()
        {
            return flag;
        }
    }

    @Test
    public void checkHasObservers()
    {
        Observable observable = new Observable();
        ObserverUtil observer = new ObserverUtil();

        observable.addObserver(observer);
        assert(observable.hasObservers());
    }

    @Test
    public void checkNotify()
    {
        Observable observable = new Observable();
        ObserverUtil observer = new ObserverUtil();

        observable.addObserver(observer);
        observable.notify("test");
        assert(observer.getFlag());
    }
    */
}
