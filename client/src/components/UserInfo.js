import styled from 'styled-components';
import axios from 'axios';
import userStore from '../store/userStore';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import FollowModal from './followlist';
import { BREAK_POINT_TABLET, BREAK_POINT_PC } from '../constants/index';

const UserInfo = () => {
  const params = useParams();
  const userId = params.userId;
  const userStoreId = userStore((state) => state.userId);
  const API_URL = process.env.REACT_APP_API_URL;
  const [isFixing, setIsFixing] = useState(false);
  const [nickname, setNickname] = useState('');
  const [follow, setFollow] = useState(0);
  const [follower, setFollower] = useState(0);
  const [height, setHeight] = useState(0);
  const [weight, setWeight] = useState(0);
  const [profileImg, setProfileImg] = useState('');
  const [isFollowOpen, setIsFollowOpen] = useState(false);
  const [isFollowerOpen, setIsFollowerOpen] = useState(false);

  const onFollowClickButton = () => {
    setIsFollowOpen(true);
  };

  const onFollowerClickButton = () => {
    setIsFollowerOpen(true);
  };

  useEffect(() => {
    const getUser = async () => {
      try {
        const token = localStorage.getItem('accessToken');
        const res = await axios.get(
          `${API_URL}members/${userId}`,
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
  }, [userId]);

  const fixMode = () => {
    setIsFixing(true);
  };

  const save = async () => {
    try {
      const token = localStorage.getItem('accessToken');
      await axios.patch(
        `${API_URL}members/${userId}`,
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
    const res = await axios.post(`${API_URL}members/profile`, formData, {
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
              {isFixing ? (
                <>
                  <SNick>닉네임 :</SNick>
                  <SNickIn value={nickname} onChange={changeName}></SNickIn>
                </>
              ) : (
                <SNickVs>{nickname}</SNickVs>
              )}
            </SName>
            <SBody>
              <SHeight>키 :</SHeight>
              {isFixing ? (
                <SHeightIn value={height} onChange={changeHeight}></SHeightIn>
              ) : (
                <StallVs>{height}cm</StallVs>
              )}
              <SWeight>몸무게 : </SWeight>
              {isFixing ? (
                <SWeightIn value={weight} onChange={changeWeight}></SWeightIn>
              ) : (
                <SWeightVs>{weight}kg</SWeightVs>
              )}
            </SBody>
            <SFollow>
              <div className="follow">
                <SFollows>팔로우</SFollows>
                <SFollowz onClick={onFollowClickButton}>{follow}</SFollowz>
              </div>
              <div className="follow">
                <SFollower>팔로워</SFollower>
                <SFollowers onClick={onFollowerClickButton}>
                  {follower}
                </SFollowers>
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
      {isFollowOpen && (
        <FollowModal
          open={isFollowOpen}
          onClose={() => {
            setIsFollowOpen(false);
          }}
          isFollow={true}
          user={userId}
        />
      )}
      {isFollowerOpen && (
        <FollowModal
          open={isFollowerOpen}
          onClose={() => {
            setIsFollowerOpen(false);
          }}
          isFollow={false}
          user={userId}
        />
      )}
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
  /* background-color: #faf6e9; */
  height: 30vh;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;

  .userinfo {
    display: flex;
    flex-grow: 6;
    justify-content: center;
    align-items: center;
  }
`;

const SPicture = styled.div`
  width: 50%;
  display: flex;
  align-items: flex-end;
  flex-direction: column;
  border-radius: 100px;
  img {
    border-radius: 10px 10px;
  }
  input {
    margin-right: -60px;
  }
`;

const SDetail = styled.div`
  width: 30%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 20px;
  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    width: 44%;
  }
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    width: 50%;
  }
`;

const SButton = styled.div`
  display: flex;
  width: 1%;
  flex-direction: column;
  margin: 40px;
  align-items: flex-end;
  height: 160px;
  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    align-items: flex-start;
  }
`;

const SFollow = styled.div`
  display: flex;
  align-items: center;
`;

const SBody = styled.div`
  display: flex;
  align-items: center;
`;

const SName = styled.div`
  display: flex;
  font-size: x-large;
`;

const SNickIn = styled.input`
  margin: 5px;
  width: 100px;
  height: 22px;
  font-size: 20px;
`;

const SHeight = styled.div`
  margin: 5px;
`;

const SHeightIn = styled.input`
  margin: 5px;
  height: 20px;
  width: 50px;
  /* margin-left: 52px; */
`;

const SWeight = styled.div`
  margin: 5px;
  width: 52px;
  height: 20px;
`;

const SNick = styled.div`
  height: 40px;
  margin-top: 5px;
`;

const SNickVs = styled.div`
  margin: 5px;
`;

const SWeightIn = styled.input`
  margin: 5px;
  height: 20px;
  width: 35px;
`;

const SFollows = styled.div`
  margin: 5px;
  font-size: 18px;
  .follow {
    display: flex;
    flex-direction: column;
  }
`;

const SFollowz = styled.button`
  margin: 5px;
  text-align: center;
  margin-left: 15px;
  background-color: white;
  border: none;
  font-size: 20px;
  cursor: pointer;
`;

const SFollower = styled.div`
  margin: 5px;
  font-size: 18px;
`;

const SFollowers = styled.button`
  margin: 5px;
  text-align: center;
  margin-left: 18px;
  background-color: white;
  border: none;
  font-size: 20px;
  cursor: pointer;
`;

const SFix = styled.button`
  width: 70px;
  border-radius: 20px;
  border: none;
  height: 30px;
  cursor: pointer;
  :hover {
    box-shadow: 1px 1px 6px gray;
  }
`;

const SSave = styled.button`
  width: 70px;
  border-radius: 20px;
  border: none;
  height: 30px;
  cursor: pointer;
  :hover {
    box-shadow: 1px 1px 6px gray;
  }
`;

const StallVs = styled.div`
  margin: 5px;
`;

const SWeightVs = styled.div`
  margin: 5px;
`;
export default UserInfo;
