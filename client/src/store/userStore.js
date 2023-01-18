import { create } from 'zustand';

const userStore = create((set, get) => ({
  userId: '',
  setUserId: (e) => {
    set({
      userId: e,
    });
  },
}));

export default userStore;
