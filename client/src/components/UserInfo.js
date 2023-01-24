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
        const token = localStorage.getItem('accessToken');
        const res = await axios.get(
          `${backendUrl}members/${userId}`,
          {
            headers: { Authorization: token },
          },
          { withCredentials: true }
        );
        if (res) {
          setNickname(res.data.nickname);
          setFollow(res.data.followeeCnt);
          setFollower(res.data.followerCnt);
          setHeight(res.data.height);
          setWeight(res.data.weight);
          setProfileImg(res.data.profileImageUrl);
        }
      } catch (err) {
        console.log(err);
      }
      // axios 내정보받아오기
    };
    getUser();
  }, []);

  const fixMode = () => {
    setIsFixing(true);
  };

  const save = async () => {
    try {
      const token = localStorage.getItem('accessToken');
      await axios.patch(
        `${backendUrl}members/${userId}`,
        {
          nickname: nickname,
          profileImageUrl: profileImg,
          height: height,
          weight: weight,
        },
        {
          headers: { Authorization: token },
        },
        { withCredentials: true }
      );
    } catch (err) {
      window.alert(err);
    }

    setIsFixing(false);
  };

  const changeName = (e) => {
    setNickname(e.target.value);
  };

  const changeHeight = (e) => {
    setHeight(e.target.value);
  };

  const onChangeImg = async (e) => {
    const image = e.target.files[0];
    const token = localStorage.getItem('accessToken');
    const formData = new FormData();
    formData.append('image', image);
    const res = await axios.post(`${backendUrl}members/profile`, formData, {
      headers: { Authorization: token },
    });

    if (res) {
      setProfileImg(res.data.profileImageUrl);
      setIsFixing(false);
    }
  };

  const changeWeight = (e) => {
    setWeight(e.target.value);
  };
  return (
    <SWrapper>
      <SProfileWrapper>
         <div className="userinfo">
          <SPicture>
            <img
              style={{ height: '200px', width: '200px' }}
              src={profileImg}
              alt="face"
            />
          {isFixing ? (
            <input
              type="file"
              id="input-file"
              // ref={upload} //참조
              onChange={onChangeImg} // 파일이 추가되면 이벤트가 일어난다.
              accept="image/*" //모든 이미지 파일형식
            />
          ) : (
            ''
          )}
        </SPicture>
        <SDetail>
          <SName>
            <SNick>닉네임</SNick>
            {isFixing ? (
              <input
                type="file"
                id="input-file"
                // ref={upload} //참조
                onChange={onChangeImg} // 파일이 추가되면 이벤트가 일어난다.
                accept="image/*" //모든 이미지 파일형식
              />
            ) : (
              ''
            )}
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
            {/* <div className="user_heightweight"> */}
            <SBody>
              <SHeight>키</SHeight>
              {isFixing ? (
                <SHeightIn value={height} onChange={changeHeight}></SHeightIn>
              ) : (
                <StallVs>{height}</StallVs>
              )}
              <SWeight>몸무게</SWeight>
              {isFixing ? (
                <SWeightIn value={weight} onChange={changeWeight}></SWeightIn>
              ) : (
                <SWeightVs>{weight}</SWeightVs>
              )}
            </SBody>
            <SFollow>
              <div className="follow">
                <SFollows>팔로우</SFollows>
                <SFollowz>{follow}</SFollowz>
              </div>
              <div className="follow">
                <SFollower>팔로워</SFollower>
                <SFollowers>{follower}</SFollowers>
              </div>
            </SFollow>
          </SDetail>
        </div>
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
  width: 100%;
  height: 30vh;
  justify-content: space-between;
  align-items: center;
  .userinfo {
    display: flex;
    flex-grow: 6;
    justify-content: center;
  }
`;

const SPicture = styled.div`
  width: 70%;
  display: flex;
  justify-content: flex-end;
`;

const SDetail = styled.div`
  width: 30%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  /* font-size: x-large; */
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
  font-size: x-large;
`;

const SNick = styled.div`
  margin: 5px;
`;

const SHeight = styled.div`
  margin: 5px;
`;

const SHeightIn = styled.input`
  margin: 5px;
  /* margin-left: 52px; */
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
  .follow {
    display: flex;
    flex-direction: column;
  }
`;

const SFollowz = styled.div`
  margin: 5px;
  text-align: center;
`;

const SFollower = styled.div`
  margin: 5px;
`;

const SFollowers = styled.div`
  margin: 5px;
  text-align: center;
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
`;

const SWeightVs = styled.div`
  margin: 5px;
`;
export default UserInfo;
