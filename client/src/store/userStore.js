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
      //닉네임추가
      nickname: '',
      setNickname: (e) => {
        set({
          nickname: e,
        });
      },
    }),
    {
      userId: 'userid',
      //닉네임 추가
      nickname: 'nickname',
    }
  )
);

export default userStore;
