import React from 'react';
import styled from 'styled-components';
import {
  BsPersonCircle,
  BsPencilSquare,
  BsBookmarkHeart,
  BsPersonPlus,
} from 'react-icons/bs';
import { AiOutlineMessage } from 'react-icons/ai';
import { BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';
import Img from '../svg/About.png';

const About = () => {
  return (
    <Swrapper>
      <div className="container">
        <div className="title">
          <img src={Img} alt="banner" className="titleimg" />
        </div>

        <Service>
          <div className="service">
            <BsPersonCircle />
            나를 나타낼 수 있는 프로필로 변경해보세요.
            <br />
            마이프로필에서 내가 게시한 글과 좋아요한 게시글을 확인해보세요.
          </div>
          <div className="service">
            <BsPencilSquare />
            내가 바로 패셔니스타! <br /> 게시글을 작성하여 나만의 옷 스타일을
            뽐내주세요.
          </div>
          <div className="service">
            <AiOutlineMessage />
            작성자가 입은 옷 스타일이 마음에 든다면?
            <br /> 채팅을 걸어 대여에 대해 대화를 나눠보세요.
          </div>
          <div className="service">
            <BsBookmarkHeart />
            이 옷 스타일은 너무 마음에 드는데? 다음에 나도 이렇게 입어야지!
            <br /> 좋아요를 눌러 마음에 드는 코디를 저장해보세요.
          </div>
          <div className="service">
            <BsPersonPlus />
            다른 사용자를 팔로우 해서 정보를 받아보세요!
            <br />
            마이프로필에서 팔로우 팔로워 목록을 확인할 수 있어요.
          </div>
        </Service>
      </div>
    </Swrapper>
  );
};

const Swrapper = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  color: #565656;
  margin-left: 170px;
  min-width: 500px;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    margin-left: 50px;
  }

  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    margin-left: -150px;
  }
  .container {
    height: 870px;
    width: 55%;
  }
  .titleimg {
    margin-top: 50px;
    height: 380px;
    width: 880px;
    border-radius: 22px;
    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      width: 70vw;
    }
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      width: 100vw;
    }
  }
`;
const Service = styled.div`
  margin-top: 10px;
  display: flex;
  box-sizing: border-box;
  flex-direction: column;
  font-size: 15px;
  margin-left: 30px;
  @media only screen and (max-width: ${BREAK_POINT_PC}px) {
    width: 70vw;
    margin-left: -10px;
  }
  @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
    width: 100vw;
  }
  svg {
    font-size: 30px;
    margin-right: 20px;
  }
  .service {
    margin: 30px 0px 0px 80px;
    display: flex;
    align-items: center;
  }
`;
export default About;
