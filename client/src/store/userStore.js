import { create } from 'zustand';

const userStore = create((set) => ({
  userId: '',
  setUserId: (e) => {
    set({
      userId: e,
    });
  },
}));

export default userStore;
