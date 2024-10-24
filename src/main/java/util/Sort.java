package util;

import clinic.*;

public class Sort {
    private static final int OFFICE_FIRST = 1;
    private static final int IMAGING_FIRST = 2;

    /**
     * Swap the element in the list.
     * @param list the list.
     * @param i the element to swap with j.
     * @param j the minimum element to swap with i.
     */
    private static void swap(List<Appointment> list,int i, int j){
        Appointment iAppointment = list.get(i);
        Appointment jAppointment = list.get(j);
        int indexOfi = list.indexOf(iAppointment);
        int indexOfj = list.indexOf(jAppointment);
        list.set(indexOfi,jAppointment);
        list.set(indexOfj,iAppointment);
    }

    /**
     * Uses selection sort to sort the appointments by profile, date, then timeslot.
     * @param list the appointment list to be sorted
     */
    private static void sortPatient(List<Appointment> list){
        if(list.isEmpty()){
            return;
        }
        int size = list.size();
        for (int i = 0; i < size-1; i++) {
            int min = i;
            for(int j = i+1; j<size; j++) {
                if(list.get(j).getPatient().compareTo(list.get(min).getPatient()) < 0) {
                    min = j;
                }
                else if(list.get(j).compareTo(list.get(min))<0 &&
                        list.get(j).getPatient().compareTo(list.get(min).getPatient())== 0){
                    min =j;
                }
            }
            swap(list,i,min);
        }
    }

    /**
     * Uses selection sort to sort the appointments by county, date, then timeslot.
     * @param list the appointment list to be sorted.
     */
    private static void sortLocation(List<Appointment> list) {
        if (list.isEmpty()) {
            return;
        }
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            int min = i;
            for (int j = i + 1; j < size; j++) {
                Provider provj = (Provider) list.get(j).getProvider();
                Provider provmin = (Provider) list.get(min).getProvider();
                if (provj.getLocation().getCounty().compareTo(provmin.getLocation().getCounty()) < 0) {
                    min = j;
                } else if (provj.getLocation().getCounty().compareTo(provmin.getLocation().getCounty()) == 0){
                    if(list.get(j).compareTo(list.get(min)) < 0) {
                        min = j;
                    }else if(list.get(j).compareTo(list.get(min)) == 0){
                        if(provj.getProfile().getFname().compareTo(provmin.getProfile().getFname()) < 0){
                            min = j;
                        }
                    }
                }
            }
            swap(list, i, min);
        }
    }

    /**
     * Uses selection sort to sort the appointments by date, timeslot, then provider.
     * @param list the appointment list to be sorted.
     */
    private static void sortAppointment(List<Appointment> list){
        if (list.isEmpty()) {
            return;
        }
        int size = list.size();
        for (int i = 0; i < size-1; i++) {
            int min = i;
            for(int j = i+1; j<size; j++) {
                if(list.get(j).compareTo(list.get(min)) < 0) {
                    min = j;
                }
                else if(list.get(j).getProvider().compareTo(list.get(min).getProvider())<0 &&
                        list.get(j).compareTo(list.get(min)) == 0){
                    min =j;
                }
            }
            swap(list,i,min);
        }
    }

    /**
     * Sort the appointments by the appointment type, Office in front and Imaging in front and vice versa
     * @param list the appointment list
     * @param key the type of appointment in front "half" of the list
     * @return return the size of the "front half" of the list
     * return -1 if there's no that type of appointment/key
     */
    private static int sortAppointment_type(List<Appointment> list, int key) {
        int left = 0;
        int right = list.size() - 1;
        if (key == OFFICE_FIRST) {
            while (left < right) {
                //move right until we find Technician
                while (left < right && list.get(left).getProvider() instanceof Doctor) {
                    left++;
                }
                //move left until we find Doctor
                while (left < right && list.get(right).getProvider() instanceof Technician) {
                    right--;
                }
                //swap them
                if (left < right) {
                    Appointment temp = list.get(left);
                    list.set(left, list.get(right));
                    list.set(right, temp);
                    left++;
                    right--;
                }
            }
            int count = 0;
            for(int i = 0; i<list.size(); i++){
                if(list.get(i).getProvider() instanceof Doctor){
                    count++;
                }else{
                    break;
                }
            }
            return count;
        } else if (key == IMAGING_FIRST) {
            while (left < right) {
                //move right until we find Doctor
                while (left < right && list.get(left).getProvider() instanceof Technician) {
                    left++;
                }
                //move left until we find Technician
                while (left < right && list.get(right).getProvider() instanceof Doctor) {
                    right--;
                }
                //swap them
                if (left < right) {
                    Appointment temp = list.get(left);
                    list.set(left, list.get(right));
                    list.set(right, temp);
                    left++;
                    right--;
                }
            }
            int count = 0;
            for(int i = 0; i<list.size(); i++){
                if(list.get(i).getProvider() instanceof Technician){
                    count++;
                }else{
                    break;
                }
            }
            return count;
        }
        return -1;
    }

    /**
     * Use selection sort to sort the Office type appointment by county, date, then timeslot.
     * @param list the appointment list to be sorted.
     */
    private static void sortOffice(List<Appointment> list){
        if (list.isEmpty()) {
            return;
        }
        int size = sortAppointment_type(list,OFFICE_FIRST);
        if(size == -1) return;
        for (int i = 0; i < size-1; i++) {
            int min = i;
            for(int j = i+1; j<size; j++) {
                if (((Provider)list.get(j).getProvider()).getLocation().getCounty().compareTo(((Provider)list.get(min).getProvider()).getLocation().getCounty()) < 0) {
                    min = j;
                } else if (list.get(j).compareTo(list.get(min)) < 0 &&
                        ((Provider)list.get(j).getProvider()).getLocation().getCounty().compareTo(((Provider)list.get(min).getProvider()).getLocation().getCounty()) == 0) {
                    min = j;
                }
            }
            swap(list,i,min);
        }
    }

    /**
     * Use selection sort to sort the Imaging type appointment by county, date, then timeslot.
     * @param list the appointment list to be sorted.
     */
    private static void sortImaging(List<Appointment> list){
        if (list.isEmpty()) {
            return;
        }
        int size = sortAppointment_type(list,IMAGING_FIRST);
        if(size == -1) return;
        for (int i = 0; i < size-1; i++) {
            int min = i;
            for(int j = i+1; j<size; j++) {
                Provider provj = (Provider) list.get(j).getProvider();
                Provider provmin = (Provider) list.get(min).getProvider();
                if (provj.getLocation().getCounty().compareTo(provmin.getLocation().getCounty()) < 0) {
                    min = j;
                } else if (provj.getLocation().getCounty().compareTo(provmin.getLocation().getCounty()) == 0){
                    if(list.get(j).compareTo(list.get(min)) < 0) {
                        min = j;
                    }else if(list.get(j).compareTo(list.get(min)) == 0){
                        if(provj.getProfile().getFname().compareTo(provmin.getProfile().getFname()) < 0){
                            min = j;
                        }
                    }
                }
            }
            swap(list,i,min);
        }
    }

    /**
     * To sort the appointment list.
     * @param list the appointment list to be sorted.
     * @param key the "key" to tell how to sort the appointment.
     *     char sortPatient = 'P';
     *     char sortLocation = 'L';
     *     char sortAppointment = 'A';
     *     char sortOffice = 'O';
     *     char sortImaging = 'I';
     */
    public static void appointment(List<Appointment> list, char key) {
        switch(key){
            case 'P':
                sortPatient(list);
                break;
            case 'L':
                sortLocation(list);
                break;
            case 'A':
                sortAppointment(list);
                break;
            case 'O':
                sortOffice(list);
                break;
            case 'I':
                sortImaging(list);
                break;
        }
    }

    /**
     * Use selection sort to sort the Provider list.
     * @param list the Provider list to be sorted.
     */
    public static void provider(List<Provider> list) {
        if(list.isEmpty()){
            return;
        }
        int size = list.size();
        for (int i = 0; i < size-1; i++) {
            int min = i;
            for (int j = i + 1; j < size; j++) {
                if (list.get(j).getProfile().compareTo(list.get(min).getProfile()) < 0) {
                    min = j;
                }
            }
            Provider iProvder = list.get(i);
            Provider jProvider = list.get(min);
            int indexOfi = list.indexOf(iProvder);
            int indexOfj = list.indexOf(jProvider);
            list.set(indexOfi,jProvider);
            list.set(indexOfj,iProvder);
        }

    }

}
