package com.npclo.imeasurer.measure;

public class MeasurePresenter {


//    public void saveMeasurement(Measurement measurement) {
//        String s = (new Gson()).toJson(measurement);
//        Log.e(getClass().toString() + "json:", s);
//        Subscription subscribe = new MeasurementHelper().saveMeasurement(s)
//                .subscribeOn(mSchedulerProvider.io())
//                .observeOn(mSchedulerProvider.ui())
//                .subscribe(__ -> measurementView.showSuccessSave(),
//                        e -> measurementView.showSaveError(),
//                        () -> measurementView.setLoadingIndicator(false)
//                );
//        mSubscription.add(subscribe);
//    }

//    public void startMeasure() {
//        if (!isConnected()) {
//            measurementView.showBleDisconnectHint();
//            return;
//        }
//        connectionObservable
//                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristicUUID))
//                .flatMap(notificationObservable -> notificationObservable)
//                .doOnSubscribe(() -> measurementView.bleDeviceMeasuring())
//                .observeOn(mSchedulerProvider.ui())
//                .doOnNext(notificationObservable -> measurementView.showStartReceiveData())
//                .subscribe(this::handleBleResult, this::handleError);
//    }
//
//    private void handleBleResult(byte[] v) {
//        String s = HexString.bytesToHex(v);
//        int code = Integer.parseInt("8D6A", 16);
//        int length = Integer.parseInt(s.substring(0, 4), 16);
//        int angle = Integer.parseInt(s.substring(4, 8), 16);
//        int battery = Integer.parseInt(s.substring(8, 12), 16);
//        int a1 = length ^ code;
//        int a2 = angle ^ code;
//        int a3 = battery ^ code;
//        measurementView.updateMeasureData((float) a1 / 10, a2, a3);
//    }

}