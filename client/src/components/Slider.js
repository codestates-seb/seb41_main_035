import styled, { keyframes } from 'styled-components';
import slideImg01 from '../svg/Service1.png';
import slideImg02 from '../svg/Service.png';
import { BREAK_POINT_PC, BREAK_POINT_TABLET } from '../constants/index';
import { useNavigate } from 'react-router-dom';

const slideAction = keyframes`
0%{transform:translateX(0);}
45%{transform:translateX(0);}
48% {transform:translateX(calc(100%*-1))} 
97% {transform:translateX(calc(100%*-1))} 
100%{transform:translateX(calc(100% *-2))}
`;

const CssSlider = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  .container {
    width: 60%;
    margin-top: 40px;
    overflow: hidden;
    border-radius: 10px;
  }
`;
const ListSlider = styled.ul`
  width: 100%;
  height: 100%;
  white-space: nowrap;
  font-size: 0;
  animation: ${slideAction} 8s linear infinite;
  li {
    display: inline-block;
    width: 100%;
    vertical-align: top;
  }
`;

const ItemSlider = styled.div`
  cursor: pointer;

  .slideThumb {
    display: block;
    width: 100%;
    height: 300px;
    object-fit: cover;
    @media only screen and (max-width: ${BREAK_POINT_PC}px) {
      height: 200px;
    }
    @media only screen and (max-width: ${BREAK_POINT_TABLET}px) {
      height: 150px;
    }
  }
`;

const Slider = () => {
  const navigate = useNavigate();
  return (
    <CssSlider>
      <div className="container">
        <ListSlider>
          <li>
            <ItemSlider>
              <img src={slideImg01} className="slideThumb" alt="배너01" />
            </ItemSlider>
          </li>
          <li>
            <ItemSlider
              onClick={() => {
                navigate(`/about`);
              }}
            >
              <img src={slideImg02} className="slideThumb" alt="배너02" />
            </ItemSlider>
          </li>

          <li>
            <ItemSlider>
              <img src={slideImg01} className="slideThumb" alt="배너01" />
            </ItemSlider>
          </li>
        </ListSlider>
      </div>
    </CssSlider>
  );
};

export default Slider;
