import styled from 'styled-components';

const Footer = () => {
  return (
    <FWrapper>
      <div className="FooterBox">
        <a href="https://github.com/nayul34">
          {/* 아이콘삽입 */}
          <svg
            aria-hidden="true"
            className="svg-icon iconGitHub"
            width="20"
            height="20"
            viewBox="0 0 18 18"
          >
            <path
              d="M9 1a8 8 0 0 0-2.53 15.59c.4.07.55-.17.55-.38l-.01-1.49c-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82a7.42 7.42 0 0 1 4 0c1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48l-.01 2.2c0 .21.15.46.55.38A8.01 8.01 0 0 0 9 1Z"
              fill="#010101"
            ></path>
          </svg>
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
          {/* 아이콘삽입 */}
          <span>
            Frontend
            {/* 한줄 아래로 */}
            <br />
            박영선
          </span>
        </a>
      </div>
      <div className="FooterBox">
        <a href="https://github.com/01055986186">
          {/* 아이콘삽입 */}
          <span>
            Frontend
            {/* 한줄 아래로 */}
            <br />
            이승준
          </span>
        </a>
      </div>
      <div className="FooterBox">
        <a href="https://github.com/eheh12321">
          {/* 아이콘삽입 */}
          <span>
            Backend
            {/* 한줄 아래로 */}
            <br />
            이도형
          </span>
        </a>
      </div>
      <div className="FooterBox">
        <a href="https://github.com/Seongbaem">
          {/* 아이콘삽입 */}
          <span>
            Backend
            {/* 한줄 아래로 */}
            <br />
            오성범
          </span>
        </a>
      </div>
      <div className="FooterBox">
        <a href="https://github.com/hyejuc">
          {/* 아이콘삽입 */}
          <span>
            Backend
            {/* 한줄 아래로 */}
            <br />
            조혜주
          </span>
        </a>
      </div>
      <div className="FooterInfo">
        <a href="https://github.com/codestates-seb/seb41_main_035">
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
  padding: 50px;
  background-color: #d9d9d9;
  width: 1200px;
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
