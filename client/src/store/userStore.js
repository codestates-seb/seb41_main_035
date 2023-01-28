import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const userStore = create(
  persist(
    (set) => ({
      userId: '',
      setUserId: (e) => {
        set({
          userId: e,
        });
      },
    }),
    {
      userId: 'userid',
    }
  )
);

export default userStore;
