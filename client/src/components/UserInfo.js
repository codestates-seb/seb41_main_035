import styled from 'styled-components';
import axios from 'axios';
import userStore from '../store/userStore';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

const backendUrl = 'http://13.125.30.88/';

const UserInfo = () => {
  const params = useParams();
  const userId = params.userId;
  const userStoreId = userStore((state) => state.userId);

  const [isFixing, setIsFixing] = useState(false);
  const [nickname, setNickname] = useState('');
  const [follow, setFollow] = useState(0);
  const [follower, setFollower] = useState(0);
  const [height, setHeight] = useState(0);
  const [weight, setWeight] = useState(0);
  const [profileImg, setProfileImg] = useState('');

  useEffect(() => {
    const getUser = async () => {
      try {
        const userData = await axios.get(
          `${backendUrl}members/${userId}`,
          {
            headers: `Bearer wsefwefwrefwrefwfasdf`,
          },
          { withCredentials: true }
        );
      } catch (err) {
        console.log(err);
      }
      // axios 내정보받아오기
      const res = {
        data: {
          email: 'eheh12321@gmail.com',
          nickname: '이도형',
          profileImageUrl:
            'https://lh3.googleusercontent.com/a/AEdFTp43nhHiZ1fH-i0wbZcHkmGnhenCG3ROID_2FfB-SQ=s96-c',
          height: 170,
          weight: 60,
          followerCnt: 10,
          followeeCnt: 200,
        },
      };
      setNickname(res.data.nickname);
      setFollow(res.data.followeeCnt);
      setFollower(res.data.followerCnt);
      setHeight(res.data.height);
      setWeight(res.data.weight);
      setProfileImg(res.data.profileImageUrl);
    };
    getUser();
  }, []);

  const fixMode = () => {
    setIsFixing(true);
  };

  const save = async () => {
    try {
      await axios.patch(
        `${backendUrl}members/${1}`,
        {
          nickname: '수정된 닉네임',
          profileImageUrl: 'http://프사_주소',
          height: 150,
          weight: 50,
        },
        {
          headers: `Bearer wsefwefwrefwrefwfasdf`,
        },
        { withCredentials: true }
      );
    } catch (err) {
      console.log(err);
    }

    setIsFixing(false);
  };

  const changeName = (e) => {
    setNickname(e.target.value);
  };

  const changeTall = (e) => {
    setHeight(e.target.value);
  };

  const changeWeight = (e) => {
    setWeight(e.target.value);
  };
  return (
    <SWrapper>
      <SProfileWrapper>
        <SPicture>
          <img style={{ height: '200px' }} src={profileImg} alt="face" />
        </SPicture>
        <SDetail>
          <SName>
            <SNick>닉네임</SNick>
            {isFixing ? (
              <SNickIn value={nickname} onChange={changeName}></SNickIn>
            ) : (
              <SNickVs>{nickname}</SNickVs>
            )}
          </SName>
          <SBody>
            <SHeight>키</SHeight>
            {isFixing ? (
              <SHeightIn value={height} onChange={setHeight}></SHeightIn>
            ) : (
              <StallVs>{height}</StallVs>
            )}
          </SBody>
          <SBody>
            <SWeight>몸무게</SWeight>
            {isFixing ? (
              <SWeightIn value={weight} onChange={changeWeight}></SWeightIn>
            ) : (
              <SWeightVs>{weight}</SWeightVs>
            )}
          </SBody>
          <SFollow>
            <SFollows>팔로우</SFollows>
            <SFollowz>{follow}</SFollowz>
            <SFollower>팔로워</SFollower>
            <SFollowers>{follower}</SFollowers>
          </SFollow>
        </SDetail>
        <SButton>
          {!isFixing && userStoreId == userId ? (
            <SFix onClick={fixMode}>정보수정</SFix>
          ) : (
            ''
          )}
          {isFixing ? <SSave onClick={save}>저장</SSave> : ''}
        </SButton>
      </SProfileWrapper>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  width: 100%;
  min-width: 1800px;

  .commentWrap {
    input {
      width: 84%;
      height: 4vh;
    }
    button {
      width: 10%;
      height: 4.5vh;
      margin-left: 20px;
    }
  }
`;

const SProfileWrapper = styled.div`
  display: flex;
  height: 30vh;
  justify-content: center;
  align-items: center;
`;

const SPicture = styled.div`
  width: 15%;
`;

const SDetail = styled.div`
  width: 15%;
  font-size: x-large;
`;

const SButton = styled.div`
  display: flex;
  width: 15%;
  flex-direction: column;
  margin: 40px;
  align-items: flex-end;
  height: 160px;
  justify-content: space-between;
`;

const SFollow = styled.div`
  display: flex;
`;

const SBody = styled.div`
  display: flex;
`;

const SName = styled.div`
  display: flex;
`;

const SNick = styled.div`
  margin: 5px;
`;

const SHeight = styled.div`
  margin: 5px;
`;

const SHeightIn = styled.input`
  margin: 5px;
  margin-left: 52px;
`;

const SWeight = styled.div`
  margin: 5px;
`;

const SNickIn = styled.input`
  margin: 5px;
`;

const SNickVs = styled.div`
  margin: 5px;
`;


const SWeightIn = styled.input`
  margin: 5px;
`;

const SFollows = styled.div`
  margin: 5px;
`;

const SFollowz = styled.div`
  margin: 5px;
`;

const SFollower = styled.div`
  margin: 5px;
`;

const SFollowers = styled.div`
  margin: 5px;
`;

const SFix = styled.button`
  width: 70px;
  border-radius: 20px;
`;

const SSave = styled.button`
  width: 70px;
  border-radius: 20px;
`;

const StallVs = styled.div`
  margin: 5px;
  margin-left: 52px;
`;

const SWeightVs = styled.div`
  margin: 5px;
`;
export default UserInfo;
