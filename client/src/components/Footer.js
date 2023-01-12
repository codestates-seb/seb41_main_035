import styled from 'styled-components';
import { BsGithub, BsEmojiSmile } from 'react-icons/bs';

const Footer = () => {
  return (
    <FWrapper>
      <div className="FooterBox">
        <a href="https://github.com/nayul34">
          {/* 아이콘삽입 */}
          <BsGithub className="github" />
          <span>
            Frontend
            {/* 한줄 아래로 */}
            <br />
            김나율
          </span>
        </a>
      </div>
      <div className="FooterBox">
        <a href="https://github.com/yspark14">
          <BsGithub className="github" />
          <span>
            Frontend

            <br />
            박영선
          </span>
        </a>
      </div>
      <div className="FooterBox">
        <a href="https://github.com/01055986186">
          <BsGithub className="github" />
          <span>
            Frontend
            <br />
            이승준
          </span>
        </a>
      </div>
      <div className="FooterBox">
        <a href="https://github.com/eheh12321">
          <BsGithub className="github" />
          <span>
            Backend
            <br />
            이도형
          </span>
        </a>
      </div>
      <div className="FooterBox">
        <a href="https://github.com/Seongbaem">
          <BsGithub className="github" />
          <span>
            Backend
            <br />
            오성범
          </span>
        </a>
      </div>
      <div className="FooterBox">
        <a href="https://github.com/hyejuc">
          <BsGithub className="github" />
          <span>
            Backend
            <br />
            조혜주
          </span>
        </a>
      </div>
      <div className="FooterInfo">
        <a href="https://github.com/codestates-seb/seb41_main_035">
          <BsEmojiSmile className="github" />
          MainProject-035
          <br />
          뽐내조
        </a>
      </div>
    </FWrapper>
  );
};

const FWrapper = styled.div`
  display: flex;
  text-align: center;
  justify-content: space-evenly;
  background-color: #d9d9d9;
  width: 100%;
  padding: 50px;
  /* 중앙정렬 */
  margin: 0 auto;
  .FooterBox {
    cursor: pointer;

    /* :hover {
      color: #ffff;
    }
    :active {
      color: yellow;
    } */
  }
  .github {
    font-size: 30px;
    display: flex;
    margin: 0 auto;
  }
  span {
  }
  a {
    text-decoration: none;
    color: #000000;
    font-weight: 500;
    font-size: 14px;
  }
`;
export default Footer;
